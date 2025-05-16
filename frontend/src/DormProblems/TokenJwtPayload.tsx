
interface TokenJwtPayload {
    sub: string;
    id: number;
    roles: string[];
    iat: number;
    exp: number;
}

export default TokenJwtPayload;