const URL = process.env.NEXT_PUBLIC_API_URL

export async function createComment(comment, token) {
    const response = await fetch(`${URL}/comments`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(comment),
    })

    if (response.status !== 201) {
        throw new Error("Could not create comment")
    }

    return await response.json()
}

export async function deleteComment(id, token) {
    const response = await fetch(`${URL}/comments/${id}`, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${token}`,
        },
    })

    if (response.status !== 204) {
        throw new Error("Could not delete comment")
    }
}

export async function editComment(comment, token) {
    const response = await fetch(`${URL}/comments/${comment.id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(comment)
    })

    if (response.status !== 200) {
        throw new Error("Could not edit comment")
    }

    return await response.json()
}

export async function getCommentsForPost(postId) {

    const response = await fetch(`${URL}/comments?postId=${postId}`)

    if (response.status !== 200) {
        throw new Error("Could not get comments")
    }

    return await response.json()
}

export async function getComment(id) {

    const response = await fetch(`${URL}/comments/${id}`)

    if (response.status !== 200) {
        throw new Error("Could not get comment")
    }

    return await response.json()
}