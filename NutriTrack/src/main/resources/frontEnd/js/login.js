document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registerForm');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');

    const showPasswordCheckbox = document.getElementById('showPassword');

    showPasswordCheckbox.addEventListener('change', function() {
        const type = this.checked ? 'text' : 'password';
        passwordInput.type = type;

    });

    // Form submit event
    form.addEventListener('submit', function(e) {

        if (emailInput.value.trim() === '' || passwordInput.value.trim() === '') {
            e.preventDefault();
            showError('Please fill in all fields!');
        }
    });

    function showError(message) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'alert alert-danger mt-3';
        errorDiv.textContent = message;


        form.appendChild(errorDiv);


        const existingError = form.querySelector('.alert');
        if (existingError) {
            existingError.remove();
        }
    }
});
