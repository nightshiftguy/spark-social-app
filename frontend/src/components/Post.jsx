import Comments from "./Comments";
import PostLikes from "./PostReactions";
import {useState, useEffect} from 'react';
import { useApiFetch } from '../utils/api';

import './Post.css'

export default function Post( {post, loggedUserUsername, onEdit, onDelete} ){
      const [routeAndOptions, setRouteAndOptions] = useState({ route: null, options: {} });
      const {status} = useApiFetch(routeAndOptions.route, routeAndOptions.options);

    var date = new Date(post.creationTimestamp);
    var formattedDate = date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
    });

    function deletePost() {
        setRouteAndOptions({
            route: 'posts/'+post.id,
            options: {
                method: 'DELETE',
            }
        });
    }

    useEffect(()=>{if(status===204){onDelete(post.id)}},[status])

    return (
    <>
        <div className="postContainer">
            {loggedUserUsername===post.author.username && <button onClick={deletePost}>delete post</button>}
            {loggedUserUsername===post.author.username && <button onClick={()=>{onEdit(post)}}>edit post</button>}
            <h3>{post.author.username}</h3>
            <p>Created at: {formattedDate}</p>
            <p>{post.textContent}</p>
            <img src={post.imageLink}></img>
            <Comments postId={post.id} loggedUserUsername={loggedUserUsername}/>
            <PostLikes postId={post.id} loggedUserUsername={loggedUserUsername} likeCount={post.likeCount}/>
        </div>
    </>
    );
}