import PostForm from "@/components/PostForm";
import {Col, Row} from "react-bootstrap";

export default function CreatePostPage() {
    return (
        <Row>
            <Col className="mt-3" xs={12} md={{span: 8, offset: 2}}>
                <h1>Create Post</h1>
            </Col>
            <Col className="mt-3" xs={12} md={{span: 8, offset: 2}}>
                <PostForm />
            </Col>
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
