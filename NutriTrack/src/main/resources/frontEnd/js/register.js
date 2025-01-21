document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registerForm');
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const confirmPassInput = document.getElementById('confirmPass');
    const showPasswordCheckbox = document.getElementById('showPassword');


    showPasswordCheckbox.addEventListener('change', function() {
        const passwordFieldType = this.checked ? 'text' : 'password';
        passwordInput.type = passwordFieldType;
        confirmPassInput.type = passwordFieldType;
    });


    form.addEventListener('submit', function(e) {
        e.preventDefault();


        if (!nameInput.value || !emailInput.value || !passwordInput.value || !confirmPassInput.value) {
            showError('Please fill in all fields!');
            return;
        }


        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailRegex.test(emailInput.value)) {
            showError('Email is invalid');
            emailInput.value = '';
            passwordInput.value = '';
            confirmPassInput.value = '';
            return;
        }

        // Password matching check
        if (confirmPassInput.value !== passwordInput.value) {
            showError('Passwords don\'t match');
            passwordInput.value = '';
            confirmPassInput.value = '';
            return;
        }


        const passwordRegex = /^(?=.*[A-Z])(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;
        if (!passwordRegex.test(passwordInput.value)) {
            showError('The password should have at least 8 characters, at least 1 uppercase letter, and at least 1 special character');
            passwordInput.value = '';
            confirmPassInput.value = '';
            return;
        }


        form.submit();
    });


    function showError(message) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'alert alert-danger mt-3';
        errorDiv.textContent = message;

        const existingError = form.querySelector('.alert');
        if (existingError) {
            existingError.remove();
        }

        form.appendChild(errorDiv);
    }
});
