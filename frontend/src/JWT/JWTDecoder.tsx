export function parseJwt(token: string) {
    try {
        const base64Url = token.split('.')[1]; // Payload znajduje się w drugiej części (indeks 1)
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // Zmieniamy format base64, by był poprawny
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        // Parsujemy payload jako JSON
        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error("Invalid token", e);
        return null;
    }
}