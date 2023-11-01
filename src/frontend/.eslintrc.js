module.exports = {
    env: {
        node: true,
    },
    extends: [
        'plugin:vue/vue3-essential',
        'eslint:recommended',
        '@vue/eslint-config-typescript',
        'prettier',
    ],
    rules: {
        // 'semi': ['error','never'],
        'vue/require-default-prop': 'off',
        'vue/require-prop-types': 'off',
        // override/add rules settings here, such as:
        // 'vue/no-unused-vars': 'error'
    }
}
