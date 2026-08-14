import { useState, useMemo, useEffect } from "react"
import { useApiFetch } from '../api';

export default function Comments({postId}){
    const [isCollapsed, setCollapsed] = useState(true);

    const routeAndOptions = useMemo(()=>{return { route: "posts/" + postId +"/comments", options: {} }}, [postId]);
    const [currentPage, setCurrentPage] = useState(0);
   const parameters = useMemo(()=>{return { page: currentPage }}, [currentPage]);
    const {data, error, loading} = useApiFetch(routeAndOptions.route, routeAndOptions.options, isCollapsed, parameters);
    const [comments, setComments] = useState(new Map());

    useEffect(()=>{
        setComments(prev => {
          const next = new Map(prev);
          (data?.content ?? []).forEach(comment => next.set(comment.id, comment));
          return next;
        });
       },[data])

    return (
        <>
        {isCollapsed && <button onClick={()=>setCollapsed(false)}>Comments</button>}
        {!isCollapsed && (
            <>
            {loading && <p>Loading...</p>}
            {!error && !loading && [...comments.values()].length === 0 && <p>No one commented this post</p>}
            {!loading && [...comments.values()].map((comment) => <p key={comment.id}>{comment.author.username} {comment.textContent}</p>)}
            {error && <p>A network error was encountered: {error.message}</p>}
            <button onClick={() => setCollapsed(true)}>hide comments</button>
            {!error && !loading && !data?.last && <button onClick={()=>setCurrentPage(currentPage+1)}>load more comments</button>}
            </>
        )}
        </>
    )
}