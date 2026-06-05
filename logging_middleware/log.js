const TEST_SERVER_URL = "https://4.224.186.213/evaluation-service/logs";

async function Log(stack, level, pkg, message) {
    const payload = {
        stack: stack,
        level: level,
        package: pkg,
        message: message
    };

    try {
        const response = await fetch(TEST_SERVER_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Log API call failed:", error);
        return null;
    }
}