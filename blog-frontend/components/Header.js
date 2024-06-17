import {Container, Nav, Navbar} from "react-bootstrap";
import Link from "next/link";
import {useSession} from "@/lib/hooks/session";
import {useRouter} from "next/router";

export default function Header() {
    const {session, isSignedIn, destroySession} = useSession()
    const router = useRouter()

    const handleLogOut = () => {
        destroySession()
    }

    const handleLoginClick = async () => {
        await router.replace(`/login?url=${router.asPath}`, null, {shallow: true})
    }

    return (
        <header>
            <Navbar expand="lg" className="bg-body-tertiary">
                <Container>
                    <Link className="navbar-brand" href="/">JEA Blog</Link>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Link href="/" className="nav-link">Home</Link>
                            {isSignedIn && <Link href="/posts/create" className="nav-link">Create Post</Link>}
                        </Nav>
                        {
                            isSignedIn ? <Nav>
                                    <Navbar.Text className="me-2">User: {session.user.displayName}</Navbar.Text>
                                    <button className="nav-link" onClick={handleLogOut}>Log out</button>
                                </Nav>
                                :
                                <Nav>
                                    <button className="nav-link" onClick={handleLoginClick}>Login</button><span
                                    className="navbar-text">/</span><Link className="nav-link"
                                                                          href="/register">Register</Link>
                                </Nav>
                        }
                    </Navbar.Collapse>
                </Container>
            </Navbar>
        </header>
    )
}