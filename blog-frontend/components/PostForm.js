import {useRouter} from "next/router"
import {useEffect, useState} from "react"
import styles from "./PostForm.module.css"
import {Alert, Button, Col, Form, Row} from "react-bootstrap";
import {useSession} from "@/lib/hooks/session";
import {createPost, deletePost, updatePost} from "@/lib/api/posts";
import CustomMarkdown from "@/components/CustomMarkdown";

const defaultModel = {
    title: "",
    content: ""
}

function validateModel(post) {
    const errors = {
        title: "",
        content: ""
    }
    let isValid = true

    if (post.title.trim().length < 1) {
        errors.title = "Title must not be empty"
        isValid = false
    }

    if (post.content.trim().length < 1) {
        errors.content = "Text must not be empty"
        isValid = false
    }

    return {errors, isValid}
}

export default function PostForm({postToEdit}) {
    const router = useRouter()
    const [isLoading, setIsLoading] = useState(false)
    const [errors, setErrors] = useState(defaultModel)
    const [post, setPost] = useState(defaultModel)
    const {session} = useSession()

    useEffect(() => {
        if (postToEdit) {
            setPost(postToEdit)
        }
    }, [postToEdit])

    const handleChange = (e) => {
        const name = e.target.name
        const value = e.target.value
        console.log(value)
        setPost({
            ...post,
            [name]: value
        })
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setIsLoading(true)
        setErrors({...defaultModel, submit: ""})

        const result = validateModel(post)

        if (!result.isValid) {
            setErrors(result.errors)
            setIsLoading(false)
            return
        }


        if (post.id) {
            try {
                const result = await updatePost(post, session.token)
                await router.push(`/posts/${result.id}`)
            } catch (e) {
                setErrors({
                    ...errors,
                    submit: e.message
                })
            }
        } else {
            try {
                const result = await createPost(post, session.token)
                await router.push(`/posts/${result.id}`)
            } catch (e) {
                setErrors({
                    ...errors,
                    submit: e.message
                })
            }
        }
        setIsLoading(false)
    }

    const handleDelete = async (id) => {
        try {
            await deletePost(id, session.token)
            await router.push("/")
        } catch (error) {
            console.error(error)
            alert("Could not delete post")
        }
    }

    return (
        <Row className={styles.postform}>
            <Col xs={12} md={6}>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label>Title</Form.Label>
                        <Form.Control name="title" type="text" placeholder="Title" onChange={handleChange}
                                      value={post.title}/>
                    </Form.Group>
                    {errors.title && <Alert variant="danger">{errors.title}</Alert>}
                    <Form.Group className="mb-3">
                        <Form.Label>Text</Form.Label>
                        <Form.Control name="content" as="textarea" rows={3} placeholder="Markdown enabled"
                                      onChange={handleChange} value={post.content}/>
                    </Form.Group>
                    {errors.content && <Alert variant="danger">{errors.content}</Alert>}
                    <Button disabled={isLoading} type="submit">
                        {isLoading ? "...Loading" : postToEdit ? "Save Changes" : "Create Post"}
                    </Button>
                    {
                        postToEdit &&
                        <Button type="button" variant="danger" onClick={() => handleDelete(post.id)}>Delete</Button>
                    }
                    {errors.submit && <Alert className="mt-3" variant="danger">{errors.submit}</Alert>}
                </Form>
            </Col>
            <Col xs={12} md={6}>
                <h1>{post.title}</h1>
                <CustomMarkdown>{post.content}</CustomMarkdown>
            </Col>
        </Row>
    )
}