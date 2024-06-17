import {useEffect, useState} from "react";
import {loadPost} from "@/lib/api/posts";
import {Button, Col, Row} from "react-bootstrap";
import CustomMarkdown from "@/components/CustomMarkdown";
import {useSession} from "@/lib/hooks/session";
import {useRouter} from "next/router";
import Comment from "@/components/Comment";
import CommentForm from "@/components/CommentForm";
import {getCommentsForPost} from "@/lib/api/comments";
import Link from "next/link";
import {BsPencil} from "react-icons/bs";

export default function PostPage() {
    const [post, setPost] = useState()
    const [comments, setComments] = useState([])
    const router = useRouter()
    const {postId} = router.query
    const {isSignedIn, session} = useSession()

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

    useEffect(() => {
        if (!postId) {
            return
        }

        const loadComments = async () => {
            const commentList = await getCommentsForPost(postId);
            setComments(commentList)
        }

        loadComments()
    }, [postId])

    const handleCommentUpdate = (comment) => {
        const updatedComments = comments.map(c => {
            if (c.id === comment.id) {
                return comment
            }
            return c
        })
        setComments(updatedComments)
    }

    const addComment = (comment) => {
        setComments([...comments, comment])
    }

    const removeComment = (id) => {
        const newComments = comments.filter(c => c.id !== id)
        setComments(newComments)
    }

    return post && (
        <>
            <Row className="mt-3">
                <Col className="mt-3" xs={12} md={{span: 6, offset: 3}}>
                    <h1 className="mb-0">
                        {post.title}
                        {isSignedIn && post.author.key === session.user.key && (
                            <Link className="ms-2 text-black" href={`/posts/${post.id}/edit`}><BsPencil/></Link>
                        )}
                    </h1>

                    <p className="text-muted fst-italic">By {post.author.displayName === session.user?.displayName ? 'You' : post.author.displayName}</p>
                </Col>
                <Col className="mt-3" xs={12} md={{span: 6, offset: 3}}>
                    <CustomMarkdown>{post.content}</CustomMarkdown>
                </Col>
            </Row>
            <Row className="mt-3">
                <Col className="mt-3" xs={12} md={{span: 6, offset: 3}}>
                    <hr/>
                    <h2>Comments</h2>
                </Col>
                {
                    isSignedIn && (
                        <Col className="mt-3" xs={12} md={{span: 6, offset: 3}}>
                            <CommentForm postId={post.id} onCreate={addComment}/>
                            <hr/>
                        </Col>
                    )
                }
                {
                    comments.length > 0 ?
                        comments.map(comment => {
                            return (
                                <Col key={comment.id} className="mt-3" xs={12} md={{span: 6, offset: 3}}>
                                    <Comment postId={post.id} comment={comment} onDelete={removeComment}
                                             onUpdate={handleCommentUpdate}/>
                                </Col>
                            )
                        })
                        :
                        <Col className="mt-3" xs={12} md={{span: 6, offset: 3}}>
                            <p className="text-muted fst-italic">There are no comments yet... Be the first to
                                respond!</p>
                        </Col>
                }
            </Row>
        </>
    )
}