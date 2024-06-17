import {Col, Row} from "react-bootstrap";
import LoginForm from "@/components/LoginForm";

export default function LoginPage() {
    return (
        <Row className="mt-3">
            <h1>Login</h1>
            <Col md={{span: 6, offset: 3}}>
                <LoginForm />
            </Col>
        </Row>
    )
}