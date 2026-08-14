import { useState, useMemo, useEffect } from "react"
import { useApiFetch } from '../api';

export default function PostLikes({postId, likeCount}){
    const [isCollapsed, setCollapsed] = useState(true);

    const routeAndOptions = useMemo(()=>{return { route: "posts/" + postId +"/reactions", options: {} }}, [postId]);
    const [currentPage, setCurrentPage] = useState(0);
   const parameters = useMemo(()=>{return { page: currentPage }}, [currentPage]);
    const {data, error, loading} = useApiFetch(routeAndOptions.route, routeAndOptions.options, isCollapsed, parameters);
    const [reactions, setReactions] = useState(new Map());

    useEffect(()=>{
        setReactions(prev => {
          const next = new Map(prev);
          (data?.content ?? []).forEach(reaction => next.set(reaction.id, reaction));
          return next;
        });
       },[data])

    return (
        <>
        {isCollapsed && <button onClick={()=>setCollapsed(false)}>Reactions {likeCount}</button>}
        {!isCollapsed && ( //change this to dialog
            <>
            {loading && <p>Loading...</p>}
            {!error && !loading && [...reactions.values()].length === 0 && <p>No one reacted to this post</p>}
            {!loading && [...reactions.values()].map((reaction) => <p key={reaction.id}>{reaction.author.username} {reaction.reaction}</p>)}
            {error && <p>A network error was encountered: {error.message}</p>}
            {!error && !loading && !data?.last && <button onClick={()=>setCurrentPage(currentPage+1)}>load more reactions</button>}
            <button onClick={() => setCollapsed(true)}>hide reactions</button>
            </>
        )}
        </>
    )
}