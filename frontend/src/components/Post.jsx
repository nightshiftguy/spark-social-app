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
        <div className="post-container">
            {loggedUserUsername===post.author.username &&
            <section>
                <button onClick={()=>{onEdit(post)}}>edit post</button>
                <button onClick={deletePost}>delete post</button>
            </section>}
            <section>
                <section className="post-info">
                    <h3>{post.author.username}</h3>
                    <div>Created at: {formattedDate}</div>
                </section>
                <section className="post-content">
                    <p>{post.textContent}</p>
                    {post.imageLink && <img className="post-image" src={post.imageLink}></img>}
                </section>
            </section>
            <section>
                <Comments postId={post.id} loggedUserUsername={loggedUserUsername}/>
                <PostLikes postId={post.id} loggedUserUsername={loggedUserUsername} likeCount={post.likeCount}/>
            </section>
        </div>
    </>
    );
}