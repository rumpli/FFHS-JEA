import {Alert, Button, Form} from "react-bootstrap";
import {useState} from "react";
import {login} from "@/lib/api/auth";
import {useSession} from "@/lib/hooks/session";
import Link from "next/link";
import {useRouter} from "next/router";

const defaultModel = {
    username: "",
    password: ""
}

function validateModel(model) {
    const errors = {
        username: "",
        password: ""
    }
    let isValid = true

    if (!model.username) {
        errors.username = "Username is required."
        isValid = false
    }

    if (!model.password) {
        errors.password = "Password is required."
        isValid = false
    }

    return {isValid, errors}
}

export default function LoginForm() {
    const [user, setUser] = useState(defaultModel)
    const [errors, setErrors] = useState(defaultModel)
    const {createSession} = useSession()
    const router = useRouter()

    const handleChange = (e) => {
        setUser({...user, [e.target.name]: e.target.value})
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
            const loginResponse = await login(user)
            await createSession(loginResponse)
            const url = router.query.url
            if (url) {
                await router.push(url)
            } else {
                await router.push("/")
            }

        } catch (e) {
            setErrors({...errors, login: "Login failed"})
        }
    }

    return (
        <>
            <Form>
                <Form.Group controlId="loginUsername">
                    <Form.Label>Username</Form.Label>
                    <Form.Control type="text" name="username" placeholder="Enter username" value={user.username}
                                  onChange={handleChange}/>
                </Form.Group>

                <Form.Group controlId="loginPassword">
                    <Form.Label>Password</Form.Label>
                    <Form.Control type="password" name="password" placeholder="Enter password" value={user.password}
                                  onChange={handleChange}/>
                </Form.Group>
                <Button className="mt-3" variant="primary" type="submit" onClick={handleSubmit}>
                    Submit
                </Button>
                <Link href="/register" className="mt-3 d-block">No account yet? Register here</Link>
            </Form>
            {errors.login && <Alert variant="danger" className="mt-3">{errors.login}</Alert>}
        </>
    )
}