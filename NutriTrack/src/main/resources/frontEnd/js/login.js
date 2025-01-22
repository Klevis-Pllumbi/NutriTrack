window.onload = function () {
    const form = document.querySelector('#registerForm');
    const emailInput = document.querySelector('#email');
    const passwordInput = document.querySelector('#password');
    const showPasswordCheckbox = document.querySelector('#showPassword');

    // Toggle password visibility
    showPasswordCheckbox.onchange = function () {
        passwordInput.setAttribute('type', this.checked ? 'text' : 'password');
    };

    // Form submit event
    form.onsubmit = function (e) {
        const emailValue = emailInput.value.trim();
        const passwordValue = passwordInput.value.trim();

        // Remove existing error messages
        const existingError = document.querySelector('.alert');
        if (existingError) {
            existingError.remove();
        }

        if (!emailValue || !passwordValue) {
            e.preventDefault();
            displayError('Please fill in all fields!');
        }
    };

    function displayError(message) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'alert alert-danger mt-3';
        errorDiv.textContent = message;

        form.appendChild(errorDiv);
    }
};