import {useState} from "react";
import {Alert, Button, Form} from "react-bootstrap";
import {register} from "@/lib/api/auth";
import {useSession} from "@/lib/hooks/session";
import Link from "next/link";
import {useRouter} from "next/router";

const defaultModel = {
    username: "",
    email: "",
    password: "",
    confirmPassword: ""
}

function validateModel(model) {
    const errors = {
        username: "",
        email: "",
        password: "",
        confirmPassword: ""
    }
    let isValid = true;

    if (!model.username.trim()) {
        errors.username = "Username is required"
        isValid = false
    }

    if (!model.email.trim()) {
        errors.email = "Email is required"
        isValid = false
    }

    if (!model.password.trim()) {
            errors.password = "Password is required"
            isValid = false
        }

    if (!model.confirmPassword.trim()) {
        errors.confirmPassword = "Confirm Password is required"
        isValid = false
    }

     if (model.password.trim() !== model.confirmPassword.trim()) {
        errors.confirmPassword = "Passwords do not match"
        isValid = false
    }

    return {isValid, errors}
}

export default function RegisterForm() {
    const [user, setUser] = useState(defaultModel)
    const [errors, setErrors] = useState(defaultModel)
    const {createSession} = useSession()
    const router = useRouter()

    const handleChange = (e) => {
        const {name, value} = e.target
        setUser({...user, [name]: value})
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setErrors(defaultModel)

        const result = validateModel(user)

        if (!result.isValid) {
            setErrors(result.errors)
            return
        }

        try {
            const registerResponse = await register(user)
            await createSession(registerResponse)
            await router.push("/")
        } catch (error) {
            setErrors({...errors, register: "Registration failed"})
        }
    }

    return (
        <Form>
            <Form.Group className="mt-3" controlId="formBasicUsername">
                <Form.Label>Username</Form.Label>
                <Form.Control type="text" placeholder="Enter username" name="username" onChange={handleChange}/>
            </Form.Group>
            {errors.username && <Alert className="mt-3" variant="danger">{errors.username}</Alert>}
            <Form.Group className="mt-3" controlId="formBasicEmail">
                <Form.Label>Email address</Form.Label>
                <Form.Control type="email" placeholder="Enter email" name="email" onChange={handleChange}/>
            </Form.Group>
            {errors.email && <Alert className="mt-3" variant="danger">{errors.email}</Alert>}
            <Form.Group className="mt-3" controlId="formBasicPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control type="password" placeholder="Enter password" name="password" onChange={handleChange}/>
            </Form.Group>
            {errors.password && <Alert className="mt-3" variant="danger">{errors.password}</Alert>}
            <Form.Group className="mt-3" controlId="formBasicPassword">
                <Form.Label>Confirm password</Form.Label>
                <Form.Control type="password" placeholder="Confirm password" name="confirmPassword" onChange={handleChange}/>
            </Form.Group>
            {errors.confirmPassword && <Alert className="mt-3" variant="danger">{errors.confirmPassword}</Alert>}
            <Button type="submit" variant="primary" className="mt-3" onClick={handleSubmit}>Register</Button>
            <Link className="mt-3 d-block" href="/login">Already have an account? Login</Link>
            {errors.register && <Alert className="mt-3" variant="danger">{errors.register}</Alert>}
        </Form>
    )
}