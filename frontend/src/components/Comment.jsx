export default function Comment({comment, isOwnComment, onDelete, onEdit, onEditCancel, isEditing, setIsEditing, commentToEdit, setCommentToEdit}){
    function startEditingComment(){
        if(!isEditing){
            setIsEditing(true); 
            setCommentToEdit(comment);
        }
    }

    function submitEditCommentForm(e){
        e.preventDefault();
        let newComment = Object.fromEntries(new FormData(e.target));
        newComment.id = comment.id
        onEdit(newComment);
    };

    return (<div>
        {!(commentToEdit?.id === comment.id) && <p>{comment.author.username} {comment.textContent}</p>}
        { isOwnComment(comment.id) && 
        <>
        <button onClick={()=>onDelete(comment.id)}>delete comment</button> 
        {!isEditing && <button onClick={startEditingComment}>edit comment</button>}
        {isEditing && commentToEdit?.id === comment.id && 
            <form onSubmit={submitEditCommentForm}>
                <label htmlFor="textContent">Your comment</label>
                <input type="text" name="textContent" defaultValue={comment.textContent}/>
                <button>Save comment</button>
            </form>
        }
        {isEditing && commentToEdit?.id === comment.id && <button onClick={onEditCancel}>Cancel editing</button>}
        </>
        }
    </div>);
}