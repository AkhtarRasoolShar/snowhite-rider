package com.example.data.repository

import com.example.data.model.CareProduct
import com.example.data.model.CustomerReview
import com.example.data.model.GarmentItem
import com.example.data.model.ItemCategory

object CatalogData {
    val garmentItems = listOf(
        // Men
        GarmentItem("m1", "2-Piece Suit (Coat & Pants)", ItemCategory.MEN, 650, "dry_cleaning", "Dry cleaned, steam pressed, & hung on shape hanger.", "Best Seller"),
        GarmentItem("m2", "Dress Shirt", ItemCategory.MEN, 220, "checkroom", "Stain pre-treatment, collar crisp pressing & bagging."),
        GarmentItem("m3", "Trousers / Cotton Pants", ItemCategory.MEN, 250, "checkroom", "Crease-perfection steam pressing."),
        GarmentItem("m4", "Shalwar Kameez (2-Piece)", ItemCategory.MEN, 380, "dry_cleaning", "Gentle wash or dry clean with sharp cuff & collar press.", "Popular"),
        GarmentItem("m5", "Kurta / Waistcoat", ItemCategory.MEN, 300, "dry_cleaning", "Special delicate care for embroidered waistcoats."),
        GarmentItem("m6", "Heavy Blazer / Jacket", ItemCategory.MEN, 750, "dry_cleaning", "Deep eco-solvent dry cleaning."),
        GarmentItem("m7", "Silk Tie / Muffler", ItemCategory.MEN, 150, "dry_cleaning", "Ultra-gentle dry clean."),

        // Women
        GarmentItem("w1", "3-Piece Fancy / Lawn Suit", ItemCategory.WOMEN, 550, "dry_cleaning", "Stain removal, color lock treatment, steam iron.", "Best Seller"),
        GarmentItem("w2", "Abaya / Hijab Set", ItemCategory.WOMEN, 480, "dry_cleaning", "Deep black tint lock & delicate steam press.", "Popular"),
        GarmentItem("w3", "Dupatta / Heavy Shawl", ItemCategory.WOMEN, 280, "dry_cleaning", "Gentle silk & pashmina dry care."),
        GarmentItem("w4", "Saree (Blouse & Petticoat)", ItemCategory.WOMEN, 950, "dry_cleaning", "Hand-finished pleat press and protection fold."),
        GarmentItem("w5", "Designer Evening Gown", ItemCategory.WOMEN, 1200, "dry_cleaning", "Sequins & beadwork safe hand cleaning."),
        GarmentItem("w6", "Silk Blouse / Top", ItemCategory.WOMEN, 320, "dry_cleaning", "Low-temp steam finish."),

        // Household
        GarmentItem("h1", "Heavy Blanket / Comforter", ItemCategory.HOUSEHOLD, 1100, "dry_cleaning", "Deep anti-allergen thermal wash & fluff dry.", "Popular"),
        GarmentItem("h2", "Double Bed Sheet Set", ItemCategory.HOUSEHOLD, 580, "dry_cleaning", "Softening wash + 2 pillowcases crisp fold."),
        GarmentItem("h3", "Window Curtains (Per Panel)", ItemCategory.HOUSEHOLD, 420, "dry_cleaning", "Dust extraction & crease-free hanging press."),
        GarmentItem("h4", "Duvet Cover Set", ItemCategory.HOUSEHOLD, 650, "dry_cleaning", "Hygienic high-temp sanitization."),
        GarmentItem("h5", "Sofa Cushion Covers (Set of 5)", ItemCategory.HOUSEHOLD, 750, "dry_cleaning", "Stain extraction & fabric refresh.")
    )

    val careProducts = listOf(
        CareProduct("p1", "SnowWhite Ultra Liquid Detergent", 850, "Detergents", "Pro-enzyme stain remover, safe for darks & colors.", 4.9, "1.5 Litres"),
        CareProduct("p2", "Wool & Silk Delicate Wash", 750, "Detergents", "pH-neutral cashmere, silk, and pashmina shampoo.", 4.8, "500 ml"),
        CareProduct("p3", "Stain Eraser Instant Pen", 450, "Care Kits", "Removes tea, coffee, oil, and sauce spots instantly on the go.", 5.0, "Pocket Size"),
        CareProduct("p4", "Pure Lavender Eco Softener", 650, "Detergents", "Static-free plush softening with organic lavender oils.", 4.9, "1 Litre"),
        CareProduct("p5", "Garment Protection Covers", 950, "Care Kits", "Breathable moth-proof suit & dress bags (Pack of 5).", 4.7, "5 Pack"),
        CareProduct("p6", "Cedarwood Suit Hangers", 1200, "Care Kits", "Contoured shoulder wood hangers to prevent collar stretch.", 4.9, "4 Pack"),
        CareProduct("p7", "Electric Lint & Pill Shaver", 1850, "Care Kits", "Rechargeable rotary shaver to restore old sweaters & coats.", 5.0, "USB-C")
    )

    val customerReviews = listOf(
        CustomerReview("r1", "Syed Bilal Raza", "Clifton, Karachi", 5, "SnowWhite is hands down the best dry cleaners in Karachi! Same-day express pickup for my suits was flawless.", "2 days ago"),
        CustomerReview("r2", "Dr. Ayesha Siddiqui", "DHA Phase 6, Karachi", 5, "My designer Abayas and silk lawn suits always come back looking brand new and smelling amazing!", "1 week ago"),
        CustomerReview("r3", "Mohammad Usman", "PECHS, Karachi", 5, "Doorstep delivery is super punctual. Live tracking showed exact driver location and status updates.", "3 days ago"),
        CustomerReview("r4", "Fatima Zahra", "Gulshan-e-Iqbal, Karachi", 5, "The detergent products and stain eraser pen are absolute lifesavers. 5 stars all the way!", "5 days ago")
    )

    val karachiAreas = listOf(
        "DHA Phase 1-8, Karachi",
        "Clifton (Blocks 1-9), Karachi",
        "PECHS (Blocks 1-6), Karachi",
        "Gulshan-e-Iqbal, Karachi",
        "North Nazimabad, Karachi",
        "Bahria Town, Karachi",
        "KDA Scheme 1 / Karsaz, Karachi",
        "Malir Cantt / Askari V, Karachi",
        "Defence View / Express Way, Karachi",
        "Saddar / I.I. Chundrigar Road, Karachi"
    )
}
