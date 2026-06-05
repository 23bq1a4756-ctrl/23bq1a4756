const TEST_SERVER_URL = "https://4.224.186.213/evaluation-service/logs";

async function Log(stack, level, pkg, message) {
    const payload = {
        stack,
        level,
        package: pkg,
        message
    };

    try {
        await fetch(TEST_SERVER_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
    } catch (err) {
        console.error("Logging failed:", err.message);
    }
}

function logMiddleware(stack = "backend", pkg = "http") {
    return async function (req, res, next) {
        const start = Date.now();

        res.on("finish", async () => {
            const duration = Date.now() - start;

            const level =
                res.statusCode >= 500 ? "error" :
                    res.statusCode >= 400 ? "warn" :
                        "info";

            const message = `${req.method} ${req.originalUrl} ${res.statusCode} (${duration}ms)`;

            await Log(stack, level, pkg, message);
        });

        next();
    };
}

module.exports = logMiddleware;