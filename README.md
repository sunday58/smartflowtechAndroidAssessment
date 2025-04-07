📦 Inventory Manager & Analytics App
An advanced Inventory Management & Analytics App built with Jetpack Compose, providing a modern, intuitive, and responsive UI for tracking stock levels, analyzing inventory trends, and optimizing product performance — all in real time.

🛠️ Features
🔍 Inventory Overview: List, search, and view all inventory items with key product info.

📉 Analytics Dashboard: Visualize low-stock and top-selling items using Jetpack Compose charts.

📊 Low Stock Detection: Automatically identifies items below a specified threshold.

🧠 Search & Filter: Real-time search and filter across inventory using powerful querying.

📱 Modern UI: Built entirely with Jetpack Compose for a fluid, native Android experience.

🚀 Offline Handling: Gracefully manages network errors and retries.

☁️ MVVM Architecture: Clean architecture using ViewModel, Repository, and StateFlow.

✅ Unit Tested: ViewModel and repository layers are fully unit tested with coroutine support.


📸 Screenshots
![Screenshot_1744061344](https://github.com/user-attachments/assets/93dde009-4199-44a7-b623-148a14fddf11)
![Screenshot_1744057748](https://github.com/user-attachments/assets/29a50644-78ea-4ead-9074-17c4b1edafec)
![Screenshot_1744057742](https://github.com/user-attachments/assets/2635bfdb-cc45-420e-a0ac-f520843ed63c)
![Screenshot_1744061965](https://github.com/user-attachments/assets/3c998bc2-971e-4878-9348-7cbd0fb7ed34)


🧱 Architecture
📦 com.example.inventoryapp
│
├── data
│   ├── model               # InventoryResponseItem, ChartItem, etc.
│   ├── repository          # InventoryRepository interface and impl
│   └── source              # Network or local data sources
│
├── domain
│   └── usecase             # Business logic
│
├── ui
│   ├── viewmodel           # GetInventoryViewModel with StateFlow
│   ├── screen              # Composables for screens: InventoryList, ChartAnalytics
│   └── component           # Reusable Jetpack Compose UI elements
│
└── utils                   # Exception handlers, UIState sealed class, etc.


📈 Tech Stack
🖌️ Jetpack Compose - Modern toolkit for building native Android UIs

⚙️ Kotlin Coroutines + Flow - Asynchronous programming

🧪 JUnit + Turbine + Mockito - Unit testing ViewModel & Repository

📊 Compose Charts (custom) - Lightweight visual analytics

🔐 Hilt - Dependency Injection

📦 MVVM - Clean and scalable architecture


🚧 How to Run
Clone the repository:

git clone https://github.com/sunday58/inventory-analytics-compose.git
cd inventory-analytics-compose

