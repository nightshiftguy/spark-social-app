import { useState } from "react"
import { useApiFetch } from '../api';
import { useMemo } from 'react';

export default function PostLikes({postId, likeCount}){
    const [isCollapsed, setCollapsed] = useState(true);

    const routeAndOptions = useMemo(()=>{return { route: "posts/" + postId +"/reactions", options: {} }}, [postId]);
    const {data, error, loading} = useApiFetch(routeAndOptions.route, routeAndOptions.options, isCollapsed);

    const reactions = data?.content ?? [];

    return (
        <>
        {isCollapsed && <button onClick={()=>setCollapsed(false)}>Reactions {likeCount}</button>}
        {!isCollapsed && ( //change this to dialog
            <>
            {loading && <p>Loading...</p>}
            {!loading && reactions.length === 0 && <p>No one reacted to this post</p>}
            {!loading && reactions.map((reaction) => <p key={reaction.id}>{reaction.reaction} {reaction.author.username}</p>)}
            {error && <p>A network error was encountered: {error.message}</p>}
            <button onClick={() => setCollapsed(true)}>hide reactions</button>
            </>
        )}
        </>
    )
}