import {Col, Row} from "react-bootstrap";
import RegisterForm from "@/components/RegisterForm";

export default function RegisterPage() {
    return (
        <Row className="mt-3">
            <h1>Register</h1>
            <Col xs={{span: 6, offset: 3}}>
                <RegisterForm/>
            </Col>
        </Row>
    )
}