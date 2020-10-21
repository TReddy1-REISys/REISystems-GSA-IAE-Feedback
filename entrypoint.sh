#!/bin/sh
# check to see if secrets exist
if [ -d "/run/secrets" ]; then
echo "Found secrets"
if [ ! /run/secrets/key-store-password.txt ] || [ ! /run/secrets/key-password.txt ]; then
echo "ERROR: Missing java key store secret files, please make sure they exist when using TLS with this app."
exit 1
else
echo "Turning key-store-password and key-password into environment variables for use with spring."
export KEY_STORE_PASSWORD="$(cat /run/secrets/keystore-password.txt)"
export KEY_PASSWORD="$(cat /run/secrets/key-password.txt)"
fi
else
echo "No secrets found; assuming environment variables are being used"
fi
exec "${@}"
