#!/data/data/com.edge.integrated/files/termux/usr/bin/bash
# OpenCode AI - Installation script for Termux environment
# This script runs inside the Termux environment to install opencode CLI

set -e

OPENCODE_VERSION="${1:-latest}"
OPENCODE_HOME="$HOME/opencode"

echo "=== OpenCode AI Installer ==="
echo "Target: $OPENCODE_HOME"
echo "Version: $OPENCODE_VERSION"
echo ""

# Check for Node.js
if ! command -v node &> /dev/null; then
    echo "Installing Node.js..."
    pkg update -y
    pkg install -y nodejs-lts
fi

NODE_VERSION=$(node --version)
echo "Node.js version: $NODE_VERSION"

# Install opencode via npm
echo "Installing opencode-ai..."
if [ "$OPENCODE_VERSION" = "latest" ]; then
    npm install -g opencode-ai@latest
else
    npm install -g "opencode-ai@$OPENCODE_VERSION"
fi

# Verify installation
echo ""
echo "Verifying installation..."
opencode --version || echo "Warning: version check failed"

# Create config directory
mkdir -p "$OPENCODE_HOME/config"
mkdir -p "$OPENCODE_HOME/sessions"

# Create default config
cat > "$OPENCODE_HOME/config/opencode.json" << 'CONFIG'
{
  "server": {
    "port": 8080,
    "host": "127.0.0.1"
  },
  "mode": "cloud",
  "providers": {
    "preferred": "auto",
    "fallback_to_local": true
  },
  "storage": {
    "sessions_dir": "./sessions",
    "config_dir": "./config"
  }
}
CONFIG

echo ""
echo "=== Installation Complete ==="
echo ""
echo "To start OpenCode server:"
echo "  opencode serve --port 8080"
echo ""
echo "To use locally:"
echo "  opencode run \"your task here\""
