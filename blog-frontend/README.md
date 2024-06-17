## Getting Started
Install the dependencies:

```bash
npm install
# or
yarn install
```

## Development

First, run the development server:

```bash
npm run dev
# or
yarn dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

## Build and start

Before building, make sure you have a `.env` file in the root directory. You can use `.env.example` as a template.

The `.env` file is used to store the environment variables.

The `NEXT_PUBLIC_API_URL` variable is used to specify the URL of the content API.

The `NEXT_PUBLIC_AUTH_URL` variable is used to specify the auth API

```
NEXT_PUBLIC_API_URL=http://localhost:8081
NEXT_PUBLIC_AUTH_URL=http://localhost:8080/auth/api/auth
```

Build:

```Bash
npm run build
# or
yarn build
```

Start:

```Bash
npm run start
# or
yarn start
```