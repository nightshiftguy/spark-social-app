import { useState, useEffect } from "react"
import { useApiFetch } from '../utils/api';

export default function PostLikes({postId, likeCount, loggedUserUsername}){
    const [isCollapsed, setCollapsed] = useState(true);
    const [hasBeenOpen, setHasBeenOpen] = useState(false);

    const [currentPage, setCurrentPage] = useState(0);
    const [parameters, setParameters] = useState({ page: currentPage });

    const [routeAndOptions, setRouteAndOptions] = useState({ route: "posts/" + postId +"/reactions", options: {} });
    const [isLastPage, setIsLastPage] = useState(null);
    const {data, status, error, loading} = useApiFetch(routeAndOptions.route, routeAndOptions.options, !hasBeenOpen, parameters);
    
    const [reactions, setReactions] = useState(new Map());

    const loggedUserReaction = [...reactions.values()].filter((reaction)=>reaction.author.username===loggedUserUsername)?.at(0);

    useEffect(()=>{
        (()=>{
            setReactions(prev => {
            const next = new Map(prev);
            if(data?.id){
                next.set(data.id, data);
            }
            else if(status===204) {
                next.delete(loggedUserReaction.id);
            }
            else {
                (data?.content ?? []).forEach(reaction => next.set(reaction.id, reaction));
                setIsLastPage(data?.last);
            }
            return next;
        });})();
       },[data])

    function loadMoreReactions(){
        setRouteAndOptions({ route: "posts/" + postId +"/reactions", options: {} });
        setCurrentPage(currentPage+1);
        setParameters({page: currentPage+1});
    }
    
    function deleteReaction() {
        setParameters(null);
        setRouteAndOptions({
            route: "posts/" + postId +"/reactions",
            options: {
                method: 'DELETE',
            }
        });
    }

    function createReaction(reaction){
        setParameters(null);
        setRouteAndOptions({
            route: "posts/" + postId +"/reactions",
            options: {
                method: 'POST',
                body : reaction
            }
        });
    }

    return (
        <>
        {isCollapsed && <button onClick={()=>{setCollapsed(false); setHasBeenOpen(true)}}>Reactions {likeCount}</button>}
        {!isCollapsed && ( //change this to dialog
            <>
            {loading && <p>Loading...</p>}
            {!error && !loading && [...reactions.values()].length === 0 && <p>No one reacted to this post</p>}
            {!error && !loading && 
            (
                loggedUserReaction === null || loggedUserReaction === undefined
                ? <button onClick={()=>createReaction(JSON.stringify({reaction :"LIKE"}))}>like</button>
                : <button onClick={deleteReaction}>unlike</button> 
            )}
            {!loading && [...reactions.values()].map((reaction) => <p key={reaction.id}>{reaction.author.username} {reaction.reaction}</p>)}
            {error && <p>A network error was encountered: {error.message}</p>}
            {!error && !loading && !isLastPage && <button onClick={loadMoreReactions}>load more reactions</button>}
            <button onClick={() => setCollapsed(true)}>hide reactions</button>
            </>
        )}
        </>
    )
}