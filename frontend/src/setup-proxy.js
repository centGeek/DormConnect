// const { createProxyMiddleware } = require('http-proxy-middleware');
//
// module.exports = function(app) {
//     app.use(
//         '/auth',
//         createProxyMiddleware({
//             target: 'http://localhost:8091',
//             changeOrigin: true,
//         })
//     );
//     app.use(
//         '/api',
//         createProxyMiddleware({
//             target: 'http://localhost:8081',
//             changeOrigin: true,
//         })
//     );
// };