import Comments from "./Comments";
import PostLikes from "./PostReactions";
import './Post.css'

export default function Post( {post} ){
    var date = new Date(post.creationTimestamp);
    var formattedDate = date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
    });
    
    return (
    <>
        <div className="postContainer">
            <h3>{post.author.username}</h3>
            <p>Created at: {formattedDate}</p>
            <p>{post.textContent}</p>
            <img src={post.imageLink}></img>
            <Comments postId={post.id}/>
            <PostLikes postId={post.id} likeCount={post.likeCount}/>
        </div>
    </>
    );
}