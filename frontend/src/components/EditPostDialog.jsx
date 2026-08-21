import { useState, useEffect, useRef, useLayoutEffect} from "react";
import { useApiFetch } from '../utils/api';
import './EditPostDialog.css'

export default function EditPostDialog({ postToEdit, show, onClose, onUpdate}){
  const [routeAndOptions, setRouteAndOptions] = useState({ route: null, options: {} });
  const {data, error, loading} = useApiFetch(routeAndOptions.route, routeAndOptions.options);

  const dialogRef = useRef(null)

  useLayoutEffect(() => {
      if (dialogRef.current?.open && !show) {
        dialogRef.current.close()
      } else if (!dialogRef.current?.open && show) {
        dialogRef.current.showModal()
      }
  }, [show])

  function submit(e){
    e.preventDefault();
    let newFormData = Object.fromEntries(new FormData(e.target));
    let method;
    let route;
    if(postToEdit){
      method = "PUT";
      route = 'posts/' + postToEdit.id;
    }
    else{
      method = "POST";
      route = 'posts'
    }
  const formData = JSON.stringify(newFormData);

  setRouteAndOptions({
    route: route,
    options: {
      method: method,
      body: formData,
    }
  });
  };

  //update data and close dialog after successful request
  useEffect(()=>{if(!data){return} onUpdate(data); onClose()}, [data])

  return (
        <dialog ref={dialogRef} onClose={onClose} >
        <button onClick={onClose}>close</button>
        <form onSubmit={submit} className='post-form'>
            <h2>{postToEdit ? "Edit post" : "New post"}</h2>

            <input name="textContent" placeholder="text content" defaultValue={postToEdit ? postToEdit.textContent : ""}/>
            <p className="error">{error && error.textContent}</p>

            <p className="error">{error && error.message}</p>
            <button>Submit</button>

            {loading && <p>Loading...</p>}
        </form>
        </dialog>
    )
}