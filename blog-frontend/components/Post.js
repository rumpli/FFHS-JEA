import {Button, Card} from "react-bootstrap";
import {useRouter} from "next/router";
import {useSession} from "@/lib/hooks/session";
import CustomMarkdown from "@/components/CustomMarkdown";
import Link from "next/link";
import {BsPencil} from "react-icons/bs";

export default function Post({post, ...props}) {
    const router = useRouter()
    const {session, isSignedIn} = useSession()

    const handleEdit = (e) => {
        e.preventDefault()
        router.push(`/posts/${post.id}/edit`)
    }

    return (
        <Card className="mt-3" {...props}>
            <Card.Body>
                <Card.Title>
                    {post.title}
                    {isSignedIn && post.author.key === session.user.key && (
                        <Link href={`/posts/${post.id}/edit`} className="ms-2 text-black"><BsPencil/></Link>
                    )}
                </Card.Title>
                <Card.Text>
                    <CustomMarkdown>{`${post.content.slice(0, 100)}...`}</CustomMarkdown>
                </Card.Text>
                <Link className="fst-italic text-decoration-none" href={`/posts/${post.id}`}>Read more...</Link>
            </Card.Body>
            <Card.Footer className="text-muted fst-italic">Created by {post.author.displayName === session.user?.displayName ? 'You' : post.author.displayName}</Card.Footer>
        </Card>
    )
}