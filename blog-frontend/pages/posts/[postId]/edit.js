import {useEffect, useState} from "react";
import {loadPost} from "@/lib/api/posts";
import {Col, Row, Spinner} from "react-bootstrap";
import PostForm from "@/components/PostForm";
import {useRouter} from "next/router";

export default function EditPage() {
    const [post, setPost] = useState()
    const [isLoading, setIsLoading] = useState(false)
    const router = useRouter()
    const {postId} = router.query

    useEffect(() => {
        if (!postId) {
            return
        }
        const loadData = async () => {
            const data = await loadPost(postId)
            setPost(data)
        }
        loadData()
    }, [postId])

    return (
        <Row>
        {
            isLoading ?
                (
                    <Col xs={{span: 2, offset: 5}}>
                        <Spinner className="mt-3" animation="border" role="status">
                            <span className="visually-hidden">Loading...</span>
                        </Spinner>
                    </Col>
                ) :
                (
                    <Col>
                        <PostForm postToEdit={post} />
                    </Col>
                )
        }
        </Row>
    )
}

export async function getStaticProps(context) {
    return {
        props: {
            secured: true
        }
    }
}

export async function getStaticPaths(context) {
    return {
        fallback: true,
        paths: []
    }
}
