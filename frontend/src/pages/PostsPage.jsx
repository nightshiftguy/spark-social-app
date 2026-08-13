import { useApiFetch } from '../api';
import { useMemo } from 'react';
import Post from '../components/Post';

function PostsPage() {
   const routeAndOptions = useMemo(()=>{return { route: "posts", options: {} }}, []);
   const {data, error, loading} = useApiFetch(routeAndOptions.route, routeAndOptions.options);
  return (
    <>
      <h2>Posts</h2>
      {loading && <p>Loading...</p>}
      {data?.content?.map((post) =><Post key={post.id} post={post}/>)}
      {error && <p>A network error was encountered: {error.message}</p>}
    </>
  )
}

export default PostsPage