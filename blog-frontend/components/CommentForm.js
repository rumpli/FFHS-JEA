import {useEffect, useState} from "react";
import {Alert, Button, Form} from "react-bootstrap";
import {createComment, editComment} from "@/lib/api/comments";
import {useSession} from "@/lib/hooks/session";

const defaultModel = {
    content: "",
    postId: 0,
}

function validateModel(model) {
    const errors = {
        content: "",
        postId: 0
    }
    let isValid = true

    if (!model.content.trim()) {
        errors.content = "Content is required!"
        isValid = false
    }

    return {isValid, errors}
}

export default function CommentForm({postId, onCreate, onUpdate, commentToEdit}) {
    const [comment, setComment] = useState(defaultModel)
    const [errors, setErrors] = useState(defaultModel)
    const {session} = useSession()

    useEffect(() => {
        if (commentToEdit) {
            setComment(commentToEdit)
        }
    }, [commentToEdit])

    const onChange = (e) => {
        const {name, value} = e.target
        setComment({...comment, [name]: value})
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setErrors(defaultModel)

        const result = validateModel(comment)

        if (!result.isValid) {
            setErrors(result.errors)
            return
        }

        if (!postId) {
            setErrors({...errors, submit: 'Unexpected error, please try again later'})
        }

        try {
            if (comment.id) {
                const response = await editComment({...comment, postId}, session.token)
                onUpdate(response)
                setComment(defaultModel)
            } else {
                const response = await createComment({...comment, postId}, session.token)
                onCreate({...response, author: {displayName: session.user.displayName, key: session.user.key}})
                setComment(defaultModel)
            }
        } catch (e) {
            setErrors({...errors, submit: e.message})
        }
    }

    return (
        <Form>
            <Form.Group className="mt-3">
                <Form.Label>Comment</Form.Label>
                <Form.Control
                    name="content"
                    value={comment.content}
                    onChange={onChange}
                    placeholder="Write a comment..."
                />
            </Form.Group>
            <Button className="mt-3" variant="outline-primary" onClick={handleSubmit}>Comment</Button>
            {errors.submit && <Alert variant="danger" className="mt-3">{errors.submit}</Alert>}
        </Form>
    )
}