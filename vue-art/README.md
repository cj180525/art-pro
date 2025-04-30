# art-design-pro

此模板应能帮助你开始使用 Vite 开发 Vue 3 项目。

## 推荐的 IDE 设置

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar)（并禁用 Vetur）。

## `.vue` 导入的 TS 类型支持

TypeScript 默认无法处理 `.vue` 导入的类型信息，因此我们用 `vue-tsc` 替代 `tsc` CLI 进行类型检查。在编辑器中，我们需要 [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) 使 TypeScript 语言服务能够识别 `.vue` 类型。

## 自定义配置

请参阅 [Vite 配置参考](https://vite.dev/config/)。

## 项目设置

```sh
pnpm install
```

### 编译并热重载以进行开发

```sh
pnpm dev
```

### 类型检查、编译并压缩以用于生产

```sh
pnpm build
```

### 使用 [Vitest](https://vitest.dev/) 运行单元测试

```sh
pnpm test:unit
```

### 使用 [ESLint](https://eslint.org/) 进行代码检查

```sh
pnpm lint
```
