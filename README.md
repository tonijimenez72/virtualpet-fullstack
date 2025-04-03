# Project Presentation ✅

## 1. Artificial Intelligence Used 🤖

I used **ChatGPT PRO** as an assistant throughout the entire project.

🎯 **Why?**
- Free AIs as ChatGPT and Deepseek have many limitations.
- ChatGPT PRO allows me to solve doubts quickly.

📌 **Practical Tips**
- Start by extracting a *checklist* from the project requirements.
- Regularly provide the project structure (`tree /f`).
- Communicate step by step, as if working in pair programming.
- If you feel stuck in a loop with the AI, ask it: “Give another approach to this problem.”
- ChatGPT is not a magic wand. Always maintain a critical view of what it suggests.

---

## 2. The Backend 📚

Developed using **Spring Boot**, following the MVC architecture.

### 🔧 Tools & Technologies:
- Spring Boot 3, Spring Security, MongoDB.
- Swagger (OpenAPI) for testing and documentation.
- JUnit + Mockito for unit testing.

### 🔁 Approach:
- Layered separation (controller, service, model, etc.).
- Custom validations and exception handling.
- Role-based access control using JWT.
- DTOs and MongoDB for persistence.

---

## 3. The Frontend 🧪

Built with **Angular 17**, using standalone components.

### 🎯 Why Angular?
- Clear and robust architecture.
- Native integration with forms and validation.
- Supported by Google and used in the IT Academy project.

### 🧩 Adopted Approach:
- Functional structure: `pages`, `components`, `services`, `models`
- `HttpClient` for API communication
- `AuthGuard` to protect routes
- Global error handling with `ErrorService`
- Pagination for admins, floating form for pet creation (user-only feature)

---

## 4. Frontend-Backend Integration 🔄

### 🔐 JWT Authentication:
- Login returns a JWT token
- The token is stored in `localStorage`
- Angular automatically sends it with every HTTP request

### 🔗 Flow:
- Angular → Backend → MongoDB → response
- Spring Security validates the token and grants access based on roles (`USER`, `ADMIN`)

### 🧪 Testing:
- Performed using **Swagger UI**
- Swagger allows logic validation before frontend integration

---

## 5. Final Thoughts 🌱

✨ What I’ve learned:
- Strengthened my skills in Java + Spring Boot
- Integrated frontend and backend with real-world security
- Used AI as an ally, not a substitute
- Improved structure, clarity, and testing practices

## 6. Goals accomplised
📄 See the full checklist in [checklist.md](./checklist.md)

## 7. Next steps
⚛️ To further enhance the project, consider implementing the following impr
ovements:

- Improve DTOs using Java Records.
- Enhance Global Exception Handling (Unique class with exception logger).
- Improve the data extraction from the token and improve decoupling.
- Implement OAuth2 Authentication.
- Evaluate Domain Model Design: Anemic vs. Rich Domain Objects.
- Develop Integration Tests with Rest Assured.
- Adopt Outside-In TDD Approach.