export class User {
    id: number;
    username: string;
    email: string;
    password: string;
    role: string;

    auth: {
        accessToken?: string;
        refreshToken?: string;
        tokenType?: string;
    }
}
