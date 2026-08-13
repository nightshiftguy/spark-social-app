import { useState } from "react"
import { useApiFetch } from '../api';
import { useMemo } from 'react';

export default function Comments({postId}){
    const [isCollapsed, setCollapsed] = useState(true);

    const routeAndOptions = useMemo(()=>{return { route: "posts/" + postId +"/comments", options: {} }}, [postId]);
    const {data, error, loading} = useApiFetch(routeAndOptions.route, routeAndOptions.options, isCollapsed);

    const comments = data?.content ?? [];

    return (
        <>
        {isCollapsed && <button onClick={()=>setCollapsed(false)}>Comments</button>}
        {!isCollapsed && (
            <>
            {loading && <p>Loading...</p>}
            {!loading && comments.length === 0 && <p>No one commented this post</p>}
            {!loading && comments.map((comment) => <p key={comment.id}>{comment.textContent}</p>)}
            {error && <p>A network error was encountered: {error.message}</p>}
            <button onClick={() => setCollapsed(true)}>hide comments</button>
            </>
        )}
        </>
    )
}