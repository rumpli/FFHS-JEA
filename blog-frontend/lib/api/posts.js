const URL = process.env.NEXT_PUBLIC_API_URL

export async function loadPosts() {
    const response = await fetch(`${URL}/posts`)

    if (response.status !== 200) {
        throw new Error("Post could not be loaded")
    }

    return await response.json()
}

export async function loadPost(id) {
    const response = await fetch(`${URL}/posts/${id}`)

    if (response.status !== 200) {
        throw new Error("Post could not be loaded")
    }

    return await response.json()
}

export async function createPost(post, token) {

    const response = await fetch(`${URL}/posts`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(post)
    })

    if (response.status !== 201) {
        throw new Error("Post could not be created")
    }

    return await response.json()
}

export async function deletePost(id, token) {

    const response = await fetch(`${URL}/posts/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })

    if (response.status !== 204) {
        throw new Error("Post could not be deleted")
    }
}

export async function updatePost(post, token) {
    const response = await fetch(`${URL}/posts/${post.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(post)
        }
    )

    if (response.status !== 200) {
        throw new Error("Post could not be updated")
    }

    return await response.json()
}