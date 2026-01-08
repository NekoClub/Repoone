# Manual Testing Plan for Evil Features

This document outlines the comprehensive testing plan for the "evil" security features implemented in Repoone Vault.

## Test Environment Setup

### Prerequisites
- Android device or emulator running Android 7.0 (API 24) or higher
- App installed and ready to test
- Backup any test data before testing auto-wipe features

---

## Test Cases

### 1. Initial PIN Setup - Complexity Enforcement

**Test ID**: TC-001  
**Feature**: PIN Complexity Requirements  
**Priority**: High

**Test Steps**:
1. Launch app for first time
2. Attempt to set PIN: `1234` (too short)
   - **Expected**: Error message "PIN must be at least 6 digits for maximum security"
3. Attempt to set PIN: `123456` (sequential)
   - **Expected**: Error message about PIN complexity
4. Attempt to set PIN: `111111` (repeated)
   - **Expected**: Error message about PIN complexity
5. Attempt to set PIN: `654321` (reverse sequential)
   - **Expected**: Error message about PIN complexity
6. Attempt to set PIN: `112233` (too many repeats)
   - **Expected**: Error message about PIN complexity
7. Set valid complex PIN: `139752`
   - **Expected**: Success, prompts for confirmation
8. Confirm PIN: `139752`
   - **Expected**: PIN set successfully, proceed to main screen

**Pass Criteria**: All weak PINs rejected, only complex PIN accepted

---

### 2. Progressive Lockout System

**Test ID**: TC-002  
**Feature**: Exponential Lockout Delays  
**Priority**: Critical

**Test Steps**:
1. Launch app
2. Enter incorrect PIN (1st attempt)
   - **Expected**: "Wrong PIN! Attempt 1 of 5" message
   - **Expected**: No lockout, can try again immediately
3. Enter incorrect PIN (2nd attempt)
   - **Expected**: "Too many failed attempts (2/5). Locked out for 10 seconds."
   - **Expected**: 10-second countdown displayed
   - **Expected**: Input disabled during countdown
4. Wait for 10-second lockout to expire
   - **Expected**: Input re-enabled automatically
5. Enter incorrect PIN (3rd attempt)
   - **Expected**: "Too many failed attempts (3/5). Locked out for 30 seconds."
   - **Expected**: 30-second countdown
6. Wait for lockout to expire
7. Enter incorrect PIN (4th attempt)
   - **Expected**: "Too many failed attempts (4/5). Locked out for 120 seconds."
   - **Expected**: 2-minute countdown

**Pass Criteria**: Lockout durations match: 0s, 10s, 30s, 120s

---

### 3. Auto-Wipe After 5 Failed Attempts

**Test ID**: TC-003  
**Feature**: Vault Auto-Wipe  
**Priority**: Critical

⚠️ **WARNING**: This test will delete all vault contents!

**Test Steps**:
1. Add several test images to vault
2. Exit app and relaunch
3. Enter incorrect PIN 4 times
   - **Expected**: Progressive lockouts as per TC-002
4. Wait for final lockout to expire
5. Enter incorrect PIN (5th attempt)
   - **Expected**: Non-dismissible dialog appears
   - **Expected**: Title: "Security Breach - Vault Wiped"
   - **Expected**: Message explains all contents deleted
6. Tap "OK"
   - **Expected**: All images deleted from vault
   - **Expected**: PIN cleared
   - **Expected**: Activity recreates, back to initial setup

**Pass Criteria**: After 5 failed attempts, vault is completely wiped and user must set new PIN

---

### 4. No Exit Without Authentication

**Test ID**: TC-004  
**Feature**: Trapped Authentication Screen  
**Priority**: High

**Test Steps**:
1. Launch app to PIN entry screen
2. Press device back button
   - **Expected**: Toast message "You must enter your PIN to access the vault..."
   - **Expected**: App does NOT exit
3. Try pressing back button multiple times
   - **Expected**: Same toast each time, no exit
4. Enter correct PIN
   - **Expected**: Proceed to main screen
5. Test back button on main screen
   - **Expected**: App minimizes (moves to background) instead of exiting

**Pass Criteria**: Cannot exit without correct PIN, back button minimizes on main screen

---

### 5. Screenshot Prevention

**Test ID**: TC-005  
**Feature**: FLAG_SECURE Implementation  
**Priority**: Medium

**Test Steps**:
1. Launch app and enter correct PIN
2. Navigate to main vault screen with images
3. Attempt to take screenshot (Power + Volume Down)
   - **Expected**: Screenshot blocked or shows black screen
4. Open screen recorder
5. Attempt to record app screen
   - **Expected**: Recording shows black screen or notification that recording is blocked
6. Navigate to different app
7. Take screenshot of other app
   - **Expected**: Screenshot works normally (confirms FLAG_SECURE is app-specific)

**Pass Criteria**: Screenshots and recordings blocked within vault app only

---

### 6. Anti-Debugging Detection

**Test ID**: TC-006  
**Feature**: Debugger Detection  
**Priority**: Medium

**Test Steps**:
1. Connect device to Android Studio
2. Enable USB debugging
3. Launch app normally (without debugger)
   - **Expected**: App launches normally
4. Attach debugger to running app process
   - **Expected**: App detects debugger and exits
   - **Expected**: Toast message "Security alert: Debugger detected..."
5. Try launching app with debugger attached from start
   - **Expected**: App exits immediately on onCreate

**Pass Criteria**: App refuses to run when debugger is attached

---

### 7. PIN Expiry Warning System

**Test ID**: TC-007  
**Feature**: PIN Age Tracking and Warnings  
**Priority**: Medium

**Note**: This test requires time manipulation or code modification for testing

**Test Steps**:
1. Set PIN and note timestamp
2. Modify device system time to 25 days later
3. Launch app and enter correct PIN
   - **Expected**: Toast warning "Security Warning: Your PIN expires in 5 days..."
4. Navigate around app
5. Minimize and resume app multiple times
   - **Expected**: Every 3rd resume shows reminder toast
6. Change system time to 30+ days later
7. Launch app and enter correct PIN
   - **Expected**: Non-dismissible dialog "Your PIN has expired..."
   - **Expected**: Forced to change PIN immediately
   - **Expected**: Can only access Settings to change PIN

**Pass Criteria**: Warnings at 25 days, forced change at 30 days

---

### 8. PIN Change Complexity Enforcement

**Test ID**: TC-008  
**Feature**: Complex PIN on Change  
**Priority**: High

**Test Steps**:
1. Navigate to Settings
2. Tap "Change PIN"
3. Enter current PIN correctly
4. Attempt new PIN: `123456` (sequential)
   - **Expected**: Rejected with complexity error
5. Attempt new PIN: `111111` (repeated)
   - **Expected**: Rejected with complexity error
6. Attempt new PIN: Same as current PIN
   - **Expected**: Rejected with "New PIN must be different"
7. Enter valid complex new PIN: `857394`
8. Confirm new PIN: `857394`
   - **Expected**: "PIN changed successfully"
9. Exit and relaunch app
10. Try old PIN
    - **Expected**: Rejected
11. Enter new PIN
    - **Expected**: Success

**Pass Criteria**: Only complex PINs accepted, cannot reuse old PIN

---

### 9. Access Logging

**Test ID**: TC-009  
**Feature**: Audit Trail  
**Priority**: Low

**Note**: Requires viewing encrypted SharedPreferences or debug logging

**Test Steps**:
1. Enter correct PIN
   - **Expected**: Success access logged
2. Enter incorrect PIN
   - **Expected**: Failed access logged
3. Check encrypted SharedPreferences
   - **Expected**: Keys like "access_log_[timestamp]"
   - **Expected**: Values are "success" or "failed"
4. Wait 30+ days (or modify timestamps)
5. Trigger log cleanup
   - **Expected**: Logs older than 30 days removed

**Pass Criteria**: All access attempts logged, old logs cleaned up

---

### 10. Real-Time Lockout Countdown

**Test ID**: TC-010  
**Feature**: Live Countdown Display  
**Priority**: Medium

**Test Steps**:
1. Trigger lockout (2 failed attempts for 10s lockout)
2. Observe title text
   - **Expected**: "LOCKED OUT - 10 seconds remaining"
   - **Expected**: Counts down: 9, 8, 7, 6...
   - **Expected**: Updates every second
3. Wait for countdown to reach 0
   - **Expected**: Title changes to "Unlock Vault"
   - **Expected**: Input re-enabled
4. Trigger longer lockout (3 failed attempts for 30s)
5. Observe countdown
   - **Expected**: Counts from 30 down to 0
6. Trigger longest lockout (4 failed attempts for 120s)
   - **Expected**: Counts from 120 down to 0

**Pass Criteria**: Countdown displays accurate remaining time and updates every second

---

## Edge Cases and Stress Tests

### 11. Rapid PIN Attempts

**Test ID**: TC-011  
**Priority**: Medium

**Test Steps**:
1. Rapidly enter incorrect PINs
2. Try to overwhelm lockout system
   - **Expected**: Lockouts enforced correctly
   - **Expected**: Input disabled during lockout
   - **Expected**: No way to bypass with rapid tapping

---

### 12. App Lifecycle During Lockout

**Test ID**: TC-012  
**Priority**: High

**Test Steps**:
1. Trigger lockout (30-second lockout)
2. While locked out, press home button
3. Open another app
4. Wait 15 seconds
5. Return to vault app
   - **Expected**: Countdown continues from where it left off
6. Rotate device during lockout
   - **Expected**: Countdown persists through configuration change
7. Force close app during lockout
8. Relaunch app
   - **Expected**: Lockout still enforced based on timestamp

---

### 13. Force Close and State Recovery

**Test ID**: TC-013  
**Priority**: High

**Test Steps**:
1. Set PIN and add images
2. Force close app
3. Relaunch
   - **Expected**: PIN entry required
4. During lockout, force close
5. Relaunch
   - **Expected**: Lockout still active
6. After 4 failed attempts, force close
7. Relaunch and fail 5th attempt
   - **Expected**: Auto-wipe still triggers

---

## Performance Tests

### 14. Log Cleanup Performance

**Test ID**: TC-014  
**Priority**: Low

**Test Steps**:
1. Generate 1000+ access logs
2. Trigger cleanup
3. Measure time taken
   - **Expected**: < 1 second for cleanup
   - **Expected**: No UI freezing

---

### 15. Complex PIN Validation Performance

**Test ID**: TC-015  
**Priority**: Low

**Test Steps**:
1. Test PIN validation with various lengths
2. Test with 20-digit PINs
3. Measure validation time
   - **Expected**: < 100ms for validation
   - **Expected**: No perceivable lag

---

## Security Tests

### 16. Encrypted Storage Verification

**Test ID**: TC-016  
**Priority**: Critical

**Test Steps**:
1. Set PIN: `123456`
2. Use ADB to view SharedPreferences file
3. Examine contents
   - **Expected**: PIN not stored in plaintext
   - **Expected**: Encrypted with AES-256-GCM
4. Examine access logs
   - **Expected**: Encrypted keys and values

---

### 17. Memory Leak Testing

**Test ID**: TC-017  
**Priority**: Medium

**Test Steps**:
1. Use Android Profiler
2. Trigger lockout countdown
3. Monitor memory usage
4. Rotate device multiple times during countdown
5. Check for Handler leaks
   - **Expected**: No memory leaks
   - **Expected**: Handler properly removed on destroy

---

## Usability Tests

### 18. User Experience - First Time Setup

**Test ID**: TC-018  
**Priority**: High

**Test Steps**:
1. Install app fresh
2. Launch for first time
3. Observe user flow
   - **Expected**: Clear instructions for PIN setup
   - **Expected**: Complexity requirements explained
   - **Expected**: Cannot skip/cancel PIN setup

---

### 19. Error Message Clarity

**Test ID**: TC-019  
**Priority**: Medium

**Test Steps**:
1. Trigger each error condition
2. Read error messages
   - **Expected**: Clear, understandable messages
   - **Expected**: Actionable guidance
   - **Expected**: No technical jargon

---

## Regression Tests

### 20. Existing Features Still Work

**Test ID**: TC-020  
**Priority**: High

**Test Steps**:
1. Add image from gallery
   - **Expected**: Works as before
2. Take photo with camera
   - **Expected**: Works as before
3. Edit image
   - **Expected**: All editing features work
4. Share image
   - **Expected**: Sharing works correctly
5. Delete image
   - **Expected**: Deletion works with confirmation

**Pass Criteria**: No existing features broken by evil features

---

## Test Execution Checklist

- [ ] TC-001: PIN Complexity Enforcement
- [ ] TC-002: Progressive Lockout System
- [ ] TC-003: Auto-Wipe (⚠️ Destructive)
- [ ] TC-004: No Exit Without Auth
- [ ] TC-005: Screenshot Prevention
- [ ] TC-006: Anti-Debugging Detection
- [ ] TC-007: PIN Expiry Warnings
- [ ] TC-008: PIN Change Complexity
- [ ] TC-009: Access Logging
- [ ] TC-010: Real-Time Countdown
- [ ] TC-011: Rapid PIN Attempts
- [ ] TC-012: App Lifecycle During Lockout
- [ ] TC-013: Force Close State Recovery
- [ ] TC-014: Log Cleanup Performance
- [ ] TC-015: Validation Performance
- [ ] TC-016: Encrypted Storage
- [ ] TC-017: Memory Leak Testing
- [ ] TC-018: UX First Time Setup
- [ ] TC-019: Error Message Clarity
- [ ] TC-020: Regression Tests

---

## Bug Reporting Template

When issues are found, report using this template:

**Bug ID**: [e.g., BUG-001]  
**Test Case**: [e.g., TC-003]  
**Severity**: [Critical/High/Medium/Low]  
**Summary**: [Brief description]  
**Steps to Reproduce**:
1. Step 1
2. Step 2
3. ...

**Expected Result**: [What should happen]  
**Actual Result**: [What actually happened]  
**Screenshots/Logs**: [If available]  
**Device/OS**: [e.g., Pixel 6, Android 13]

---

## Test Sign-Off

**Tester Name**: _______________  
**Date**: _______________  
**Test Environment**: _______________  
**Overall Result**: [Pass/Fail/Partial]

**Summary**:
- Tests Passed: ___ / 20
- Tests Failed: ___ / 20
- Bugs Found: ___
- Critical Issues: ___

**Recommendation**: [Ready for release / Needs fixes / More testing required]

**Notes**:
[Any additional observations or concerns]
