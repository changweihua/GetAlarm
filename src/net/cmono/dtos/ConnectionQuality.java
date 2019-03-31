package net.cmono.dtos;

public enum ConnectionQuality {
	/**
	 * Bandwidth under 150 kbps.
	 */
	POOR,
	/**
	 * Bandwidth between 150 and 550 kbps.
	 */
	MODERATE,
	/**
	 * Bandwidth between 550 and 2000 kbps.
	 */
	GOOD,
	/**
	 * EXCELLENT - Bandwidth over 2000 kbps.
	 */
	EXCELLENT,
	/**
	 * Placeholder for unknown bandwidth. This is the initial value and will
	 * stay at this value if a bandwidth cannot be accurately found.
	 */
	UNKNOWN
}
