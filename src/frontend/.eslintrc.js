module.exports = {
    env: {
        node: true,
    },
    extends: [
        'plugin:vue/vue3-essential',
        '@vue/eslint-config-typescript',
        'plugin:@typescript-eslint/recommended',
        'prettier',
    ],
    parserOptions: {
        project: true,
        tsconfigRootDir: __dirname,
        extraFileExtensions: ['.vue']
    },
    parser: "vue-eslint-parser",
    plugins: ['@typescript-eslint'],
    rules: {
        'vue/require-default-prop': 'off',
        'vue/require-prop-types': 'off',
    }
}
