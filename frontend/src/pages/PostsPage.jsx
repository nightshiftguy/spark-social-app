import { useApiFetch } from '../utils/api';
import { useMemo, useState, useEffect } from 'react';
import Post from '../components/Post';

function PostsPage() {
   const routeAndOptions = useMemo(()=>{return { route: "posts", options: {} }}, []);
   const [currentPage, setCurrentPage] = useState(0);
   const parameters = useMemo(()=>{return { page: currentPage }}, [currentPage]);
   const {data, error, loading} = useApiFetch(routeAndOptions.route, routeAndOptions.options, false, parameters);
   const [posts, setPosts] = useState(new Map());

   useEffect(()=>{
    setPosts(prev => {
      const next = new Map(prev);
      (data?.content ?? []).forEach(post => next.set(post.id, post));
      return next;
    });
   },[data])
   
  return (
    <>
      <h2>Posts</h2>
      {!error && loading && <p>Loading...</p>}
      {[...posts.values()].map((post) => <Post key={post.id} post={post} />)}
      {error && <p>A network error was encountered: {error.message}</p>}
      {!error && !loading && !data?.last && <button onClick={()=>setCurrentPage(currentPage+1)}>load more posts</button>}
    </>
  )
}

export default PostsPage