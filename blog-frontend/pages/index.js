import {useEffect, useState} from "react";
import {loadPosts} from "@/lib/api/posts";
import {Button, Col, Row, Spinner} from "react-bootstrap";
import Post from "@/components/Post";
import {useRouter} from "next/router";

export default function Home() {
    const [posts, setPosts] = useState([])
    const [isLoading, setIsLoading] = useState(false)
    const router = useRouter()

    useEffect(() => {
        setIsLoading(true)
        const loadData = async () => {
            try {
                const data = await loadPosts()

                setPosts(data)
            } catch (e) {
                console.log(e)
            }
            setIsLoading(false)
        }
        loadData()
    }, []);


    return (
        <Row className="mt-3">
            {
                isLoading ?
                    <Col xs={{span: 2, offset: 5}}>
                        <Spinner className="mt-3" animation="border" role="status">
                            <span className="visually-hidden">Loading...</span>
                        </Spinner>
                    </Col>
                    :
                    posts.length > 0 ?
                        posts.map(post => (
                            <Col lg={{span: 6, offset: 3}} className="mt-3" key={post.id}>
                                <Post post={post}/>
                            </Col>
                        ))
                        :
                        <Col xs={{span: 6, offset: 3}} className="mt-3">
                            <h1>Soo sad, there are no posts yet...</h1>
                            <p>Go ahead and create one!</p>
                            <Button variant="primary" onClick={() => router.push('/posts/create')}>Create the first post</Button>
                        </Col>

            }
        </Row>
    )
}
