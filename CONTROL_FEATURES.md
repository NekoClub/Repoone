# Techdomme/Kinkware Control Features

## Overview

Repoone includes consensual control features designed for BDSM/kink dynamics. These features allow an administrator (Dominant/Controller) to set restrictions and requirements for a controlled user (Submissive).

**⚠️ IMPORTANT**: These features are designed for **CONSENSUAL** use only between adults. Always discuss boundaries, have safe signals, and respect limits.

## Features

### 1. Dual PIN System

- **User PIN**: Regular PIN for accessing the vault
- **Admin PIN**: Separate PIN required to access control settings
- Both PINs are encrypted and stored securely

### 2. Role-Based Access

Two roles available:
- **Administrator**: Full access to all features and admin settings
- **Controlled User**: Access restricted based on admin-configured permissions

### 3. Time-Based Access Restrictions

Control when the vault can be accessed:
- Set start time (e.g., 18:00)
- Set end time (e.g., 22:00)
- Access denied outside these hours
- App closes immediately if opened outside allowed time

### 4. Check-In System

Require the controlled user to check in at regular intervals:
- Configurable interval (in hours)
- Overdue check-ins trigger alerts
- Cannot access vault without checking in when overdue
- Check-in button appears in Settings

### 5. Feature Permissions

Control which features the controlled user can access:
- **Add Images**: Allow/deny adding new images
- **Delete Images**: Allow/deny deleting images
- **Edit Images**: Allow/deny editing images
- **Share Images**: Allow/deny sharing images
- **Change PIN**: Allow/deny changing their User PIN

### 6. Activity Logging

Complete audit trail of vault activities:
- All actions logged with timestamps
- Admin can view log anytime
- Shows: logins, image operations, check-ins, etc.
- Cannot be disabled or modified by controlled user

## Setup Guide

### For Administrators

#### Initial Configuration:

1. **Install and Setup**:
   - Install the app on the controlled user's device
   - Complete first-time setup with a User PIN

2. **Set Admin PIN**:
   - Open Settings
   - Tap "Admin Settings"
   - Set an Admin PIN (keep this private!)
   - Confirm the PIN

3. **Configure User Role**:
   - In Admin Settings, select "Controlled User" from the role dropdown
   - This activates all control features

4. **Configure Access Restrictions**:
   - Enable "Restrict Access" toggle
   - Set "Access Start Time" (e.g., 18:00)
   - Set "Access End Time" (e.g., 22:00)
   - User can only access vault during this window

5. **Setup Check-Ins**:
   - Enable "Check-in Required" toggle
   - Set check-in interval (e.g., 24 hours)
   - User must check in within this interval

6. **Set Feature Permissions**:
   - Toggle each permission based on your agreement
   - Disabled features will be grayed out for the user
   - Changes apply immediately

#### Ongoing Management:

- **View Activity Log**: Admin Settings → View Access Log
- **Clear Log**: Admin Settings → Clear Access Log (careful!)
- **Modify Settings**: All require Admin PIN
- **Change Admin PIN**: Admin Settings → Change PIN (Admin)

### For Controlled Users

#### Daily Use:

1. **Login**: Use your User PIN as normal
2. **Respect Restrictions**: Access only during allowed times
3. **Check In**: When prompted, go to Settings → Check In
4. **Use Available Features**: Restricted features will be disabled

#### Check-In Process:

When check-in is overdue:
1. App will show an alert on launch
2. Tap "Check In" or go to Settings
3. Tap the check-in button
4. Confirmation will be shown
5. Continue using the app

#### Understanding Restrictions:

- **Grayed-out buttons**: Feature disabled by admin
- **Access denied messages**: Outside allowed time window
- **Check-in alerts**: Overdue for required check-in
- **Cannot modify**: Admin settings locked behind Admin PIN

## Safety & Consent

### Before Implementing Control Features:

1. **Discuss Thoroughly**:
   - What restrictions will be in place
   - Duration of restrictions
   - Check-in schedule
   - Safe signals/safewords

2. **Start Gradually**:
   - Begin with lenient settings
   - Increase restrictions slowly
   - Check in regularly about comfort levels

3. **Establish Boundaries**:
   - What's acceptable and what's not
   - How to communicate discomfort
   - Emergency procedures
   - Regular relationship check-ins

4. **Safe Signals**:
   - Have a safeword/safe signal
   - Both parties must respect it immediately
   - Regular check-ins about the dynamic

### Red Flags (For Controlled Users):

🚩 Watch out for these warning signs:
- Restrictions you didn't agree to
- No respect for safewords/boundaries
- Excessive monitoring/control
- Isolation from support systems
- Punishment beyond agreed limits
- Feeling unsafe or coerced

**If you feel unsafe, seek help immediately**. This is meant for consensual kink, not abuse.

### For Administrators:

✅ Good practices:
- Regular check-ins about comfort
- Respect all limits and boundaries
- Use access log responsibly
- Gradual implementation of restrictions
- Clear communication

❌ Never:
- Ignore safewords or boundaries
- Use control features as punishment for non-kink issues
- Share access log data without consent
- Prevent access to safety resources
- Coerce or manipulate

## Technical Details

### Security Implementation:

- **Admin PIN Storage**: Separate encrypted entry in EncryptedSharedPreferences
- **Access Time Checks**: Validated at app launch and activity resume
- **Check-In Enforcement**: Checked at app launch, blocks access if overdue
- **Permission Checks**: Evaluated before each action (add, edit, delete, share)
- **Activity Logging**: Append-only log with timestamps, stored encrypted

### Data Storage:

All control settings stored in EncryptedSharedPreferences:
- `admin_pin`: Encrypted admin PIN
- `user_role`: Current role (admin/controlled)
- `access_restricted`: Boolean for time restrictions
- `access_start_time`: Start time (milliseconds)
- `access_end_time`: End time (milliseconds)
- `check_in_required`: Boolean for check-in requirement
- `check_in_interval`: Interval in hours
- `last_check_in`: Last check-in timestamp
- `can_add_images`: Boolean permission
- `can_delete_images`: Boolean permission
- `can_edit_images`: Boolean permission
- `can_share_images`: Boolean permission
- `can_change_pin`: Boolean permission
- `access_log`: Timestamped activity log

### Reset Instructions:

If Admin PIN is forgotten:
1. Uninstall the app (loses all data)
2. Reinstall and start fresh
3. **Note**: This is by design for security

To disable control features:
1. Admin must log in with Admin PIN
2. Change role back to "Administrator"
3. Disable all restrictions
4. This cannot be done without Admin PIN

## Troubleshooting

### Common Issues:

**"Access denied: Outside allowed hours"**
- Access is restricted by time window
- Wait until allowed time or ask admin to adjust

**"Check-in is overdue!"**
- You need to check in
- Go to Settings → Check In
- Complete the check-in to continue

**"Permission denied by administrator"**
- Feature is disabled by admin
- Contact admin to discuss permissions

**"Wrong Admin PIN"**
- Admin PIN is incorrect
- Only admin can access admin settings
- If forgotten, must uninstall/reinstall (loses data)

**Feature button is grayed out**
- Feature disabled in admin settings
- Contact admin to enable if needed

## FAQ

**Q: Can controlled user see admin settings?**
A: No, admin settings require Admin PIN to access.

**Q: Can controlled user disable restrictions?**
A: No, only admin can modify restrictions with Admin PIN.

**Q: What if I forget Admin PIN?**
A: Must uninstall and reinstall app (loses all data). This is by design.

**Q: Can I use this without control features?**
A: Yes! Just don't set an Admin PIN. App works as normal vault.

**Q: Is the activity log private?**
A: Admin can view it. Discuss data privacy with your admin.

**Q: Can admin see my images?**
A: No, admin controls settings only, not image access via remote means. Physical access to device = can see images.

**Q: How do I exit this dynamic?**
A: Discuss with admin. Admin can disable all restrictions. If unsafe, uninstall app.

**Q: Is this actually secure?**
A: Yes, uses AES256-GCM encryption. But remember: physical device access bypasses most security.

## Support & Resources

### For Kink/BDSM Community:

- Always practice SSC (Safe, Sane, Consensual) or RACK (Risk-Aware Consensual Kink)
- Regular check-ins and communication
- Respect safewords and boundaries
- These are tools for consensual dynamics only

### If You Need Help:

If you're in an unsafe situation:
- **National Domestic Violence Hotline**: 1-800-799-7233
- **RAINN**: 1-800-656-4673
- Contact local authorities if in immediate danger

### Technical Support:

For app issues:
- GitHub Issues: https://github.com/NekoClub/Repoone/issues
- Check documentation: README.md, FEATURES.md

## Legal Disclaimer

This software is provided for consensual use between adults. The developers:
- Do not condone abuse, coercion, or non-consensual control
- Are not responsible for misuse of control features
- Encourage responsible and consensual use
- Recommend clear communication and boundaries

Users are responsible for ensuring all use is consensual and legal in their jurisdiction.

## Version History

### Version 1.1 (Current)
- Added dual PIN system
- Role-based access control
- Time-based access restrictions
- Check-in system
- Feature permissions
- Activity logging
- Admin settings interface

### Version 1.0
- Basic vault functionality
- Image editor
- Social media sharing
