# Development Stage
FROM node:18-alpine AS development

WORKDIR /app

COPY package*.json ./

RUN npm ci

COPY . .

EXPOSE 3000


CMD ["npm", "run", "dev"]

# Builder Stage
FROM node:18-alpine AS builder

WORKDIR /app

COPY package*.json ./

RUN npm ci

COPY . .

RUN npm run build

# Production Stage

FROM node:18-alpine AS production

WORKDIR /app

# Copy the built artifacts from the builder stage
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/package*.json ./
COPY --from=builder /app/public ./public

# Install all dependencies
RUN npm ci

# Set the environment variables (if needed)
ENV NODE_ENV=production

EXPOSE 3000

CMD ["npm", "start"]