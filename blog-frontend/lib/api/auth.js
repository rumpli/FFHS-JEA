const URL = process.env.NEXT_PUBLIC_AUTH_URL

export async function login({username, password}) {
    const response = await fetch(`${URL}/login`, {
        method: "POST",
        headers: {
            "content-type": "application/json"
        },
        body: JSON.stringify({username, password})
    })

    if (!response.ok) {
        throw new Error("An error occurred while fetching")
    }

    return await response.json()
}

export async function register({username, password, email}) {
    const response = await fetch(`${URL}/register`, {
        method: "POST",
        headers: {
            "content-type": "application/json"
        },
        body: JSON.stringify({username, password, email})
    })

    if (!response.ok) {
        throw new Error("An error occurred while fetching")
    }

    return await response.json()
}