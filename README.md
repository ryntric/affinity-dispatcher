[![Maven Central](https://img.shields.io/maven-central/v/io.github.ryntric/workers.svg)](https://search.maven.org/artifact/io.github.ryntric/affinity-dispatcher)
[![LICENSE](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/ryntric/workers/blob/master/LICENSE)
[![Maven CI](https://github.com/ryntric/workers/actions/workflows/maven-build.yml/badge.svg)](https://github.com/ryntric/affinity-dispatcher/actions/workflows/maven-build.yml)
[![pages-build-deployment](https://github.com/ryntric/workers/actions/workflows/pages/pages-build-deployment/badge.svg)](https://github.com/ryntric/affinity-dispatcher/actions/workflows/pages/pages-build-deployment)

# 🚀 Affinity Dispatcher

**Affinity Dispatcher** is a high-performance, low-latency message routing framework in Java.
It routes messages to workers based on key affinity and supports multiple key types.

---

## 📚 Table of Contents

- [Overview](#overview)  
- [Key Components](#key-components) 

---

## Overview

The dispatcher uses a **multiply-high scaling algorithm** to map hash codes to routing nodes efficiently.  

✨ **Features:**  

- Lock-free internal channels (SPSC / MPSC)  
- Configurable worker count & routing nodes  
- Branchless and consistent key routing  
- Low-latency message handling  
- Atomic state management (started, non-started, terminated)

---

## Key Components

### 🏗️ `AffinityDispatcher<T>`

Main dispatcher for routing messages to workers.  

**Supports:** `int`, `long`, `String`, `byte[]` keys  

**Important Methods:**  

- `dispatch(String key, T value)`  
- `dispatch(int key, T value)`  
- `dispatch(long key, T value)`  
- `dispatch(byte[] key, T value)`  
- `start()` ✅  
- `shutdown()` 🛑  

### 👷 `Worker<T>`

Worker thread consuming messages from its channel.  

### 🗂️ `RoutingNode<T>`

Routing table node holding a reference to a worker and publishing messages.  

### 📡 `Channel<T>`

Internal message queue:  

- **SPSC** – Single-producer, single-consumer  
- **MPSC** – Multi-producer, single-consumer  

---

## 🛠️ Usage Example

```java
Handler<Object> handler = (workerName, value) -> {
    System.out.println("Handled by " + workerName + ": " + value);
};
AffinityDispatcher<Object> dispatcher = new AffinityDispatcher<>("test", handler, DefaultHashCodeProvider.INSTANCE, Config.builder().build());
dispatcher.start();

for (int i = 0; i < 10_000_000; i++) {
    dispatcher.dispatch(UUID.randomUUID().toString(), i);
}
```
