#!/data/data/com.edge.integrated/files/termux/usr/bin/bash
# OpenCode AI - Server startup script
# Starts opencode in server mode, accessible from Edge Gallery UI

set -e

OPENCODE_HOME="$HOME/opencode"
PORT="${1:-8080}"
MODE="${2:-cloud}"
CONFIG_FILE="$OPENCODE_HOME/config/opencode.json"

echo "=== OpenCode AI Server ==="
echo "Port: $PORT"
echo "Mode: $MODE"
echo ""

# Ensure opencode is installed
if ! command -v opencode &> /dev/null; then
    echo "Error: opencode not found."
    echo "Run install-opencode.sh first."
    exit 1
fi

# Update config for this session
if [ -f "$CONFIG_FILE" ]; then
    tmp=$(mktemp)
    cat "$CONFIG_FILE" | sed "s/\"port\": [0-9]*/\"port\": $PORT/" | \
        sed "s/\"mode\": \"[a-z]*\"/\"mode\": \"$MODE\"/" > "$tmp"
    mv "$tmp" "$CONFIG_FILE"
fi

# Start server
echo "Starting OpenCode server on port $PORT..."
echo "Mode: $MODE"
echo ""

opencode serve --port "$PORT" --host "127.0.0.1"

echo "Server stopped."
