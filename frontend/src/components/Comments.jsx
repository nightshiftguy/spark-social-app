import { useState, useEffect } from "react"
import { useApiFetch } from '../utils/api';

import Comment from "./Comment";

export default function Comments({postId, loggedUserUsername}){
    const [isCollapsed, setCollapsed] = useState(true);
    const [hasBeenOpen, setHasBeenOpen] = useState(false);

    const [currentPage, setCurrentPage] = useState(0);
    const [parameters, setParameters] = useState({ page: currentPage });

    const [isLastPage, setIsLastPage] = useState(null);
    const [routeAndOptions, setRouteAndOptions] = useState({ route: "posts/" + postId +"/comments", options: {} });
    const {data, status, error, loading} = useApiFetch(routeAndOptions.route, routeAndOptions.options, !hasBeenOpen, parameters);
    
    const [comments, setComments] = useState(new Map());
    const [commentIdToDelete, setCommentIdToDelete] = useState();

    const [isEditing, setIsEditing] = useState(false);
    const [commentToEdit, setCommentToEdit] = useState(null);

    function findComment(id){ return comments.get(id); }
    function isOwnComment(id){ return findComment(id)?.author.username === loggedUserUsername; }

    useEffect(()=>{
        (()=>{
            setComments(prev => {
            const next = new Map(prev);
            if(data?.id){
                next.set(data.id, data);
            }
            else if(status===204 && commentIdToDelete !== null) {
                setComments(prev => {const next = prev; next.delete(commentIdToDelete); return next});
            }
            else {
                (data?.content ?? []).forEach(comment => next.set(comment.id, comment));
                setIsLastPage(data?.last);
            }
            return next;
        });
        setCommentIdToDelete(null);
        })();
       },[data])

    function loadMoreComments(){
        setRouteAndOptions({ route: "posts/" + postId +"/comments", options: {} });
        setCurrentPage(currentPage+1);
        setParameters({page: currentPage+1});
    }
    
    function deleteComment(commentId) {
        setParameters(null);
        setRouteAndOptions({
            route: "posts/" + postId +"/comments/"+commentId,
            options: {
                method: 'DELETE',
            }
        });
        setCommentIdToDelete(commentId);
    }

    function createComment(comment){
        const commentJSON = JSON.stringify(comment);
        setParameters(null);
        setRouteAndOptions({
            route: "posts/" + postId +"/comments",
            options: {
                method: 'POST',
                body : commentJSON
            }
        });
        setIsEditing(false);
        setCommentToEdit(null);
    }

    function cancelEditing(){
        setIsEditing(false);
        setCommentToEdit(null);
    }

    function editComment(comment){
        const commentJSON = JSON.stringify(comment);
        console.log(comment)
        setParameters(null);
        setRouteAndOptions({
            route: "posts/" + postId +"/comments/"+ comment.id,
            options: {
                method: 'PUT',
                body : commentJSON
            }
        });
        setIsEditing(false);
        setCommentToEdit(null);
    }

    function submitNewCommentForm(e){
        e.preventDefault();
        let comment = Object.fromEntries(new FormData(e.target));
        createComment(comment);
    };

    return (
        <>
        {isCollapsed && <button onClick={()=>{setCollapsed(false); setHasBeenOpen(true);}}>Comments</button>}
        {!isCollapsed && (
            <>
            {loading && <p>Loading...</p>}
            {!error && !loading && [...comments.values()].length === 0 && <p>No one commented this post</p>}
            {!loading && [...comments.values()].map((comment) => 
                <Comment key={comment.id} comment={comment} isOwnComment={isOwnComment} onDelete={deleteComment} onEdit={editComment} onEditCancel={cancelEditing} isEditing={isEditing} setIsEditing={setIsEditing} commentToEdit={commentToEdit} setCommentToEdit={setCommentToEdit}/>
            )}
            {error && <p>A network error was encountered: {error.message}</p>}
            {!isEditing && <button onClick={()=>setIsEditing(true)}>create comment</button>}
            {isEditing && commentToEdit === null && 
                <form onSubmit={submitNewCommentForm}>
                    <label htmlFor="textContent">Your comment</label>
                    <input type="text" name="textContent" />
                    <button>Save comment</button>
                </form>
            }
            {isEditing && commentToEdit === null && <button onClick={()=>{setIsEditing(false); setCommentToEdit(null)}}>Cancel editing</button>}
            <button onClick={() => setCollapsed(true)}>hide comments</button>
            {!error && !loading && !isLastPage && <button onClick={loadMoreComments}>load more comments</button>}
            </>
        )}
        </>
    )
}