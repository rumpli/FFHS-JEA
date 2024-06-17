import {useState} from "react";
import CommentForm from "@/components/CommentForm";
import {useSession} from "@/lib/hooks/session";
import {Button} from "react-bootstrap";
import {deleteComment} from "@/lib/api/comments";
import {BsPencil, BsTrash} from "react-icons/bs";

export default function Comment({comment, onUpdate, onDelete, postId}) {
    const [edit, setEdit] = useState(false)
    const {isSignedIn, session} = useSession()

    const handleDelete = async () => {
        try {
            await deleteComment(comment.id, session.token)
            onDelete(comment.id)
            setEdit(false)
        } catch (e) {
            alert('Something went wrong')
        }
    }

    const handleUpdate = (comment) => {
        setEdit(false)
        onUpdate(comment)
    }

    return (
        edit ?
            <>
                <CommentForm commentToEdit={comment} onUpdate={handleUpdate} postId={postId}/>

                {isSignedIn && session.user.key === comment.author.key &&
                    <Button className="mt-3" variant="outline-secondary" onClick={() => setEdit(false)}>Cancel</Button>}
                <hr/>
            </>
            :
            <>
                <h3>
                    Comment by {comment.author.displayName}
                    {isSignedIn && session.user.key === comment.author.key &&
                        <Button className="ms-2" variant="outline-secondary" onClick={() => setEdit(true)}><BsPencil/></Button>}
                    {isSignedIn && session.user.key === comment.author.key &&
                        <Button className="ms-2" variant="outline-danger" onClick={handleDelete}><BsTrash/></Button>}
                </h3>
                <p>{comment.content}</p>
                <hr/>
            </>
    )
}