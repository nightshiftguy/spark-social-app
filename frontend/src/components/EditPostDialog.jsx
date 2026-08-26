import { useState, useEffect, useRef, useLayoutEffect} from "react";
import { useApiFetch} from '../utils/api';
import './EditPostDialog.css'

export default function EditPostDialog({ postToEdit, show, onClose, onUpdate}){
  const [postRouteAndOptions, setPostRouteAndOptions] = useState({ route: null, options: {} });
  const {data: postData, error: postError, loading: postLoading} = useApiFetch(postRouteAndOptions.route, postRouteAndOptions.options);

  const [imageRouteAndOptions, setImageRouteAndOptions] = useState({ route: null, options: {} });
  const {error: imageError} = useApiFetch(imageRouteAndOptions.route, imageRouteAndOptions.options);

  const dialogRef = useRef(null)

  //toggle dialog
  useLayoutEffect(() => {
      if (dialogRef.current?.open && !show) {
        dialogRef.current.close()
      } else if (!dialogRef.current?.open && show) {
        dialogRef.current.showModal()
      }
  }, [show])

  //on form submit
  function submit(e) {
    e.preventDefault();
    const rawFormData = new FormData(e.target);

    const method = postToEdit ? 'PATCH' : 'POST';
    const route = postToEdit ? `posts/${postToEdit.id}` : 'posts';

    setPostRouteAndOptions({
      route: route,
      options: {
        method: method,
        body: rawFormData,
      },
    });
  }

  function deleteImage(){
    setImageRouteAndOptions({
      route: 'posts/'+postToEdit.id+'/image',
      options: {
        method: 'DELETE',
      },
    });

    let newPost = { ...postToEdit };
    newPost.imageLink = null;
    onUpdate(newPost);
    onClose();
  }

  //continue after sending post
  useEffect(()=>{
    if(!postData){return} 
    onUpdate(postData); onClose();
  }, [postData])

  return (
        <dialog ref={dialogRef} onClose={onClose} >
        <button onClick={onClose}>close</button>
        <form onSubmit={submit} className='post-form'>
            <h2>{postToEdit ? "Edit post" : "New post"}</h2>

            {postToEdit?.imageLink && <button onClick={deleteImage}>Delete image</button>}
            <label htmlFor="image">{postToEdit?.imageLink ? "Override image" : "Add image"}</label>
            <input name="image" type="file" accept="image/png, image/jpeg"/>
            <p className="error">{imageError && imageError.message}</p>

            <input name="textContent" placeholder="text content" defaultValue={postToEdit ? postToEdit.textContent : ""}/>
            <p className="error">{postError && postError.textContent}</p>

            <p className="error">{postError && postError.message}</p>
            <button>Submit</button>

            {postLoading && <p>Loading...</p>}
        </form>
        </dialog>
    )
}