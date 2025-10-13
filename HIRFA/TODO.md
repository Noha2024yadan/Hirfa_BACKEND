# TODO: Fix Compilation Errors

## 1. Product Entity Field Mismatches
- [x] Update CartService.java to use correct field names: `getStockQuantity()` instead of `getQuantiteStock()`, `getPrice()` instead of `getPrix()`

## 2. Missing Repository Methods
- [x] Add `findByUsernameOrEmail(String username, String email)` to DesignerRepository
- [x] Add `findByUsernameOrEmail(String username, String email)` to CooperativeRepository
- [x] Add `existsByUsernameIgnoreCase(String username)` to ClientRepository
- [x] Add `existsByEmailIgnoreCase(String email)` to ClientRepository
- [x] Add `searchClients(String search, Pageable pageable)` to ClientRepository
- [x] Add `findByUsername(String username)` to ClientRepository

## 3. Client Entity Missing Fields
- [x] Add `statut` field to Client entity (boolean)

## 4. DesignerController Type Mismatch
- [x] Fix incompatible types in DesignerController.java: DesignerUpdateDto to DesignerDTO conversion

## 5. Test Compilation
- [x] Run `./mvnw compile` to verify all errors are fixed
