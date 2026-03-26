# SDUI Contract
# ALL screens must be driven by this JSON schema. No exceptions.

## Screen Response Schema
{
  "screenId": "login",
  "version": "1.0",
  "metadata": {
    "title": "Secure Portal",
    "analyticsTag": "login_screen"
  },
  "layout": {
    "type": "SCROLL" | "COLUMN" | "LAZY_COLUMN",
    "padding": { "horizontal": 24, "vertical": 32 }
  },
  "components": [ <ComponentModel> ],
  "actions": { "ACTION_ID": <ActionModel> }
}

## Component Model
{
  "id": "unique_id",
  "type": "COMPONENT_TYPE",
  "visible": true,
  "props": { <type-specific props> },
  "action": "ACTION_ID"   // optional
}

## Supported Component Types & Props

### TEXT
{ "text": "", "style": "DISPLAY|HEADING|BODY|CAPTION|LABEL", "color": "TOKEN_NAME" }

### TEXT_FIELD
{ "label": "", "hint": "", "inputType": "TEXT|EMAIL|PASSWORD|PHONE|NUMBER",
  "required": true, "validations": [{"type": "EMAIL|MIN_LENGTH|REGEX", "value": "", "message": ""}] }

### BUTTON
{ "label": "", "style": "PRIMARY|SECONDARY|GHOST|DESTRUCTIVE", "loading": false }

### ICON_BUTTON
{ "icon": "FINGERPRINT|FACE_ID|BACK", "style": "OUTLINED|FILLED", "contentDescription": "" }

### CARD
{ "elevation": 2, "children": [ <ComponentModel> ] }

### IMAGE
{ "url": "", "localAsset": "", "contentScale": "FIT|CROP|FILL", "aspectRatio": 1.0 }

### SPACER
{ "height": 16 }

### DIVIDER
{ "thickness": 1, "color": "TOKEN_NAME" }

### ROW
{ "arrangement": "START|CENTER|SPACE_BETWEEN", "children": [ <ComponentModel> ] }

### LINK_TEXT
{ "prefix": "", "linkText": "", "suffix": "" }

### BIOMETRIC_ROW
{ "options": ["FINGERPRINT", "FACE_ID"], "label": "AUTHENTICATION" }

## Action Model
{
  "type": "NAVIGATE|API_CALL|DEEP_LINK|SHOW_DIALOG|DISMISS|SUBMIT_FORM|BIOMETRIC_AUTH",
  "destination": "route",            // for NAVIGATE
  "endpoint": "/api/path",           // for API_CALL
  "method": "POST|GET",
  "params": {},
  "onSuccess": <ActionModel>,        // chained action
  "onError": <ActionModel>
}

## Login Screen JSON Example (assets/mock/screens/login_screen.json)
{
  "screenId": "login",
  "version": "1.0",
  "metadata": { "title": "Secure Portal" },
  "layout": { "type": "COLUMN", "padding": { "horizontal": 24, "vertical": 48 } },
  "components": [
    { "id": "logo", "type": "IMAGE", "props": { "localAsset": "ic_architect_logo", "aspectRatio": 1.0 } },
    { "id": "spacer_1", "type": "SPACER", "props": { "height": 48 } },
    { "id": "title", "type": "TEXT", "props": { "text": "Secure\nPortal", "style": "DISPLAY" } },
    { "id": "subtitle", "type": "TEXT", "props": { "text": "Access your institutional wealth management dashboard.", "style": "BODY" } },
    { "id": "spacer_2", "type": "SPACER", "props": { "height": 32 } },
    { "id": "username_field", "type": "TEXT_FIELD", "props": { "label": "USERNAME", "hint": "Institutional ID or Email", "inputType": "EMAIL", "required": true } },
    { "id": "password_field", "type": "TEXT_FIELD", "props": { "label": "PASSWORD", "hint": "", "inputType": "PASSWORD", "required": true }, "action": "FORGOT_PASSWORD" },
    { "id": "login_btn", "type": "BUTTON", "props": { "label": "Login", "style": "PRIMARY" }, "action": "SUBMIT_FORM" },
    { "id": "biometric", "type": "BIOMETRIC_ROW", "props": { "options": ["FINGERPRINT", "FACE_ID"], "label": "AUTHENTICATION" } },
    { "id": "register_link", "type": "LINK_TEXT", "props": { "prefix": "New to the firm? ", "linkText": "Inquire about an account", "suffix": "" }, "action": "INQUIRE" }
  ],
  "actions": {
    "SUBMIT_FORM": { "type": "API_CALL", "endpoint": "/auth/login", "method": "POST", "onSuccess": { "type": "NAVIGATE", "destination": "dashboard" }, "onError": { "type": "SHOW_DIALOG", "params": { "message": "Invalid credentials" } } },
    "FORGOT_PASSWORD": { "type": "NAVIGATE", "destination": "forgot_password" },
    "INQUIRE": { "type": "DEEP_LINK", "destination": "architect://onboarding/inquire" }
  }
}
