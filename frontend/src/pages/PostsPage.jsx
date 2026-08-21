import { useApiFetch } from '../utils/api';
import { useOutletContext } from 'react-router';
import { useMemo, useState, useEffect } from 'react';
import Post from '../components/Post';
import EditPostDialog from '../components/EditPostDialog';

function PostsPage() {
   const routeAndOptions = useMemo(()=>{return { route: "posts", options: {} }}, []);
   const [currentPage, setCurrentPage] = useState(0);
   const parameters = useMemo(()=>{return { page: currentPage }}, [currentPage]);
   const {data, error, loading} = useApiFetch(routeAndOptions.route, routeAndOptions.options, false, parameters);
   const [posts, setPosts] = useState(new Map());

   const {isLogged, loggedUserUsername} = useOutletContext();

   const [isEditPostDialogVisible, setIsEditPostDialogVisible] = useState(false);
   const [postToEdit, setPostToEdit] = useState(null);

   useEffect(()=>{
    (()=>{setPosts(prev => {
      const next = new Map(prev);
      (data?.content ?? []).forEach(post => next.set(post.id, post));
      return next;
    });})();
   },[data])

   function onEditPostDialogClose(){
      setPostToEdit(null);
      setIsEditPostDialogVisible(false);
   }

   function deletePost(postId){
    setPosts(prev => {
      const next = new Map(prev);
      next.delete(postId);
      return next;
    });
   }

   function editPost(postToEdit){
    setPostToEdit(postToEdit);
    setIsEditPostDialogVisible(true);
   }

   function updatePostData(newPost){
    setPosts(prev => {
      const next = new Map(prev);
      next.set(newPost.id, newPost);
      return next;
    });
   }
   
  return (
    <>
      <h2>Posts</h2>
      {isLogged && <button onClick={()=>setIsEditPostDialogVisible(true)}>Create new post</button>}
      {!error && loading && <p>Loading...</p>}
      {[...posts.values()].map((post) => <Post key={post.id} post={post} loggedUserUsername={loggedUserUsername} onEdit={editPost} onDelete={deletePost}/>)}
      {error && <p>A network error was encountered: {error.message}</p>}
      {!error && !loading && !data?.last && <button onClick={()=>setCurrentPage(currentPage+1)}>load more posts</button>}
      <EditPostDialog show={isEditPostDialogVisible} onClose={onEditPostDialogClose} onUpdate={updatePostData} postToEdit={postToEdit}/>
    </>
  )
}

export default PostsPage